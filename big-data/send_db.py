import psycopg2
import json
import requests

URL = "https://watchat-backend.herokuapp.com"

con = psycopg2.connect("dbname=watchatdb user=postgres")
cur = con.cursor()

def send_movie_list(movies):
    response = requests.post(f'{URL}/saveJpaMovies', json.dumps(movies), headers={'Content-Type': 'application/json'})
    print(response.text)


def send_n_movies(movies, n, start):
    list_to_send = []
    if start+n > len(movies):
        list_to_send = movies[start:]
    else:
        list_to_send = movies[start:start+n]

    send_movie_list(list(map(
        lambda m: {'id':m[0],'description':m[1],'image':m[2],'name':m[3],'redirect':m[4],'tags':get_tags_by_id(m[0]),'neighbours':get_neighbours_by_id(m[0])}, list_to_send)))


def get_tags_by_id(id):
    cur.execute("""
                SELECT match, name FROM tag WHERE tmdb_id=%s
                """, (id,))
    
    return list(map(lambda t: {'match':t[0],'name':t[1]}, cur.fetchall()))

def get_neighbours_by_id(id):
    cur.execute("""
                SELECT neighbours_tmdb_id FROM movie_neighbours WHERE movie_tmdb_id=%s
                """, (id,))

    return list(map(lambda t: t[0], cur.fetchall()))

def send_movies():
    cur.execute("""
                SELECT * FROM movie 
                    WHERE (
                        SELECT count(*) FROM movie_neighbours
                        WHERE tmdb_id=movie_tmdb_id
                    )>0
                """)
    movies_in_graph = cur.fetchall()

    i=0
    step = 150
    while i<len(movies_in_graph):
        send_n_movies(movies_in_graph, step, i)
        i += step


send_movies()

cur.close()
con.close()
