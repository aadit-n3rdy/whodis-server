# Whodis

A concurrent key-value store

The client sends request messages to the server. Keys are provided
as plaintext and values are base-64 encoded.

## Protocol

1. To get a value:

**Request:**

```
GET
<key>
```

**Response:**

```
<value in base64 encoding>
DONE
```

2. To set a value:

**Request:**

```
SET
<key>
<value>
```

**Response:**

```
DONE
```

## Usage

Compile using `mvn compile`.

To run, run the following commands:

```bash
cd target/classes/

# To run server
java com.whodis.whodis.App

# To run client
java com.whodis.whodis.Client
```

**Default IP address and port:** 127.0.0.1, 8080


