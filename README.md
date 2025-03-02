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
