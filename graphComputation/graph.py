import psycopg2
from sortedcontainers import SortedDict
from math import sqrt

DBNAME = "watchatdb"
DBUSER = "postgres"
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
      tmp = abs(match - node.tags[name])
      if(tmp > 0.2):
        res += pow(match - node.tags[name],2) * WEIGHTS[name] # weighted euclidean distance
    return sqrt(res)

  def updateNeighbours(self, node):
    self.neighbours.update([(self.dist(node),node)])
    if (len(self.neighbours) > GRAPHDEPTH):
      self.neighbours.popitem()

def getDatabaseConnection():
  return psycopg2.connect(f"dbname={DBNAME} user={DBUSER}")

def insertNeighbours(node, cur, con):
  for nxt in node.neighbours.values():
    cur.execute("""INSERT INTO movie_neighbours (movie_tmdb_id, neighbours_tmdb_id) VALUES (%s, %s)
        """, (node.idNumber, nxt.idNumber))
    con.commit()

def getNodes(cur, con):
  nodes = []
  cur.execute("SELECT tmdb_id FROM movie")
  ids = cur.fetchall()
  for idNumber in ids:
    cur.execute("SELECT name, match FROM tag WHERE tmdb_id = %s", idNumber)
    tag_dict = dict(cur.fetchall())
    if len(tag_dict):
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
