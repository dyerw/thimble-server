# Thimble Server

Backend server for an app we're making.

## Usage

Just:

```
lein run
```

```
lein test
```

## Basic Code Overview

All routes are currently in core.clj. These parse request args and give them
to route handling functions. The data-access-layer namespace exposes an API
to retrieve and create data from the database(s).

## The deal with the databases

Using SQLite to store info we need to look up easily like username/password hash.

Using Neo4j to model followers/post replies.

## Todo

- [ ] Create a macro to add auth logic to routes
- [ ] Probably move routes out of core.clj
- [ ] Create interface to Neo4j database to express relations between data
- [ ] Add file upload functionality and hook into S3

## License

Shit, I'm not a lawyer
