# Thimble Server

Backend server for an app we're making.

## Usage

```
source thimble-env
```

```
$ brew install rabbitmq

add the following to .bashrc, adding rabbitmq-server to path
PATH=$PATH:/usr/local/sbin

see: https://www.rabbitmq.com/install-homebrew.html
```

```
$ rabbitmq-server
$ rabbitmqctl add_user <username> <password>
$ rabbitmqctl set_permissions thimble ".*" ".*" ".*"
(see thimble-env for username and password)
(see https://www.rabbitmq.com/man/rabbitmqctl.1.man.html)
```

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

- [x] Create ~~a macro~~ middleware to add auth logic to routes
- [x] Probably move routes out of core.clj
- [ ] Create interface to Neo4j database to express relations between data
- [ ] Add file upload functionality and hook into S3

## License

Shit, I'm not a lawyer
