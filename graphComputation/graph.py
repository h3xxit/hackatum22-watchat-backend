import psycopg2
from sortedcontainers import SortedDict

DBNAME = "streaming-data"
DBUSER = "arne"
GRAPHDEPTH = 10
WEIGHTS = {
      'anime': 0.4,
      'car': 0.5,
      'battle': 0.8,
      'love': 1.0,
      'comedy': 0.95,
      'happy': 0.9,
      'sad': 0.85,
      'police': 0.35,
      'superhero': 0.7,
      'dystopia': 0.25,
      'popularity': 0.01
      }

class Node:

  def __init__(self, tags, idNumber):
    self.tags = tags
    self.neighbours = SortedDict()
    self.idNumber = idNumber

  def dist(self, node):
    res = 0
    for name, match in self.tags.items():
      res += pow(match - node.tags[name],2) * WEIGHTS[name] # weighted euclidean distance
    return res

  def updateNeighbours(self, node):
    self.neighbours.update([(self.dist(node),node)])
    if (len(self.neighbours) > GRAPHDEPTH):
      self.neighbours.popitem()

def getDatabaseConnection():
  return psycopg2.connect(f"dbname={DBNAME} user={DBUSER}")

def insertNeighbours(node, cur, con):
  for nxt in node.neighbours.values():
    cur.execute("""INSERT INTO edges_test (start_id, end_id) VALUES (%s, %s)
        """, (node.idNumber, nxt.idNumber))
    con.commit()

def getNodes(cur, con):
  nodes = []
  cur.execute("SELECT DISTINCT tmdb_id FROM graph_data_test")
  ids = cur.fetchall()
  for idNumber in ids:
    cur.execute("SELECT name, match FROM graph_data_test WHERE tmdb_id = %s", idNumber)
    tag_dict = dict(cur.fetchall())

    aktNode = Node(tag_dict, idNumber)
    for node in nodes:
      node.updateNeighbours(aktNode)
      aktNode.updateNeighbours(node)
    nodes.append(aktNode)
  for node in nodes:
    insertNeighbours(node, cur, con)


def cleanUp(cur, con):
  con.commit()
  cur.close()
  con.close()


con = getDatabaseConnection()
cur = con.cursor()
getNodes(cur, con)
cleanUp(cur, con)