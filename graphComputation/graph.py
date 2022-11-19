import psycopg2
from sortedcontainers import SortedDict

DBNAME = "streaming_data"
DBUSER = "arne"
GRAPHDEPTH = 10


class Node:
  DIM = 10
  WEIGHT = []

  def __init__(self, vector, idNumber):
    self.vec = vector
    self.neighbours = SortedDict()
    self.idNumber = idNumber

  def dist(self, node):
    res = 0;
    for i in range(0,Node.DIM):
      res += pos(self.vector[i] - node.vector[i],2) * Node.WEIGHT[i] # weighted euclidean distance
    return res

  def updateNeighbours(self, node):
    self.neighbours.update([(self.dist(self, node),node)])
    if (len(self.neighbours) > GRAPHDEPTH):
      self.neighbours.popitem()

def getDatabaseConnection():
  return psycopg2.connect("dbname={dbname} user={user}".format(name=DBNAME, user=DBUSER))

def insertNeighbours(node, cur):
  for nxt in node.neighbours.values():
    cur.execute("""INSERT INTO Edges (startId, endId) VALUES (%s, %s)
        """, (node.idNumber, nxt.idNumber))

def getNodes(cur):
  nodes = []
  cur.execute("SELECT id FROM Movies")
  ids = cur.fetchall()
  for idNumber in ids:
    cur.execute("SELECT * FROM tags WHERE id = %s", idNumber)
    tags = [x[1:] for x in cur.fetchall()]
    tags.sort()
    aktNode = Node([x[2] for x in tags], idNumber)
    for node in nodes:
      node.updateNeighbours(aktNode)
      aktNode.updateNeighbours(node)
    nodes.append(aktNode)
  for node in nodes:
    insertNeighbours(node, cur)


def cleanUp(cur, con):
  con.commit()
  cur.close()
  con.close()


con = getDatabaseConnection()
cur = con.cursor()
getNodes(cur)
cleanUp(cur, con)
