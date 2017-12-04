# vdmAPI

Simple API to fetch VDM stories [from official website](http://www.viedemerde.fr) and share through a REST API

## Getting Started

- Clone project
- `cd vdmAPI`
- build docker image with `sbt compile docker:publishLocal`
- start docker iamges with `docker-compose up`
- start scraper import with `docker-compose exec vdmapi bin/root -main ScraperApp`
- REST api is available at [http://localhost:9000/<API>](http://localhost:9000/<API>). Ex : `curl http://localhost:9000/api/posts | json_pp`

## Testing

- Manual tests yet using postman collection located at `/test/vdmAPI.postman_collection.json`.
- support for parsing tests using scalatest : `sbt test`

## API Description

### /api/posts

#### Output
```json
{
  "posts": [
    {
      "id": 1,
      "content": "Aujourd'hui, on m'a demandé de réaliser un test de développeur.",
      "date": "2017-01-01T00:00:00Z",
      "author": "Genius"
    } 
  ], 
  "count": 1
}
```

#### Parameters
- from (optional) ­- Date start
- to (optional) ­ Date end
- author (optionnel) ­ Author

#### Usage
- /api/posts 
- /api/posts?from=2017-01-01T00:00:00Z&to=2017-12-31T00:00:00Z
- /api/posts?author=Genius

### /api/posts/<ID>

#### Output
```json
{
  "post": {
    "id": 1,
    "content": "Aujourd'hui, on m'a demandé de réaliser un test de développeur.",
    "date": "2017-01-01T00:00:00Z",
    "author": "Genius"
  }
}
```
