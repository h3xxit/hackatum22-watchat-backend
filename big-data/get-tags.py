import json
import requests
import psycopg2
from transformers import pipeline
classifier = pipeline("zero-shot-classification",
                      model="facebook/bart-large-mnli")

HUGGINGFACE_API_URL = "https://api-inference.huggingface.co/models/facebook/bart-large-mnli"
HUGGINGFACE_API_TOKEN = "" # ENTER API TOKEN HERE
huggingface_headers = {"Authorization": f"Bearer {HUGGINGFACE_API_TOKEN}"}

candidate_labels = [
    'superhero',
    'sport',
    'criminal',
    'horror',
    'romance',
    'space',
    'fantasy',
    'comedy',
    'drama',
    'science fiction',
    'documentary',
    'fighting',
    'family'
]

def get_tags_by_description(tags: list[str], description: str) -> dict :
    result = classifier(description, tags, multi_label=True)
    print(result)
    return result
    """
    data = json.dumps({"parameters": {"candidate_labels": tags, 'multi_label': True}, "inputs": description})
    response = requests.request("POST", HUGGINGFACE_API_URL, headers=huggingface_headers, data=data)
    if response.status_code != 200:
        print(response.text)
        return None
    return json.loads(response.content.decode("utf-8"))
    """

def write_tags_to_db(tmdb_id: int, tags: dict, cur, con):
    for t in tags:
        cur.execute("INSERT INTO tag (tmdb_id, name, match) VALUES(%s, %s, %s)", (tmdb_id, t['name'], t['match']))

    con.commit()
 


con = psycopg2.connect("dbname=watchatdb user=postgres")
cur = con.cursor()

cur.execute("SELECT * FROM movie_description as md WHERE (SELECT count(*) from movie where md.tmdb_id=tmdb_id)>0 AND (SELECT count(*) from tag as t WHERE md.tmdb_id=t.tmdb_id)=0 ORDER BY popularity DESC")

tmdb_desc_pop = cur.fetchall()

j = 0
for id,desc,pop in tmdb_desc_pop:
    if desc is None or desc == "" or candidate_labels == []:
        continue
    tags = get_tags_by_description(candidate_labels, desc)
    if not tags:
        continue

    labels = tags['labels']
    scores = tags['scores']
    tags = []
    for i in range(len(labels)):
        tags.append({'name': labels[i], 'match': scores[i]})
    tags.append({'name': 'popularity', 'match': pop})
    write_tags_to_db(id, tags, cur, con)
    j += 1
    print(j)
    if j >= 1000:
        break


cur.close()
con.close()
