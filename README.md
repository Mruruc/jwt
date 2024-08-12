# JWT (JSON Web Token) Implementation in Java

Welcome to this repository! Here, you’ll find a basic custom implementation of JSON Web Tokens (JWT) in Java. This project is designed to help you understand how JWTs and JSON Web Signatures (JWS) work, all without relying on external libraries or dependencies.

## What You’ll Learn

- **JWT Basics**: Understand the core concepts behind JSON Web Tokens.
- **JWS Implementation**: Learn how to create and verify JSON Web Signatures.
- **Pure Java Approach**: Explore how to implement JWT and JWS from scratch in Java.

## Usage

This project is primarily for educational purposes. It’s not intended for production use, but you're welcome to explore, experiment, and contribute. Feel free to submit pull requests if you have improvements or enhancements!




## General Concept of JWT

### Components

- **Header**
- **Payload**
- **Signature**

#### Header

The header contains metadata about the token, including the type of the token and, if it's signed, the signing algorithm used. It is encoded as Base64Url.

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

#### Payload

The claims or the actual data being transmitted in the token.
Encoded as Base64Url.

#### Signature

Used to verify the integrity and authenticity of the token. The primary purpose of signing a JWT (JSON Web Signature, JWS) is to ensure integrity and authenticity.

- **Integrity**: Ensures that the data in the JWT (the header and payload) hasn't been altered during transmission. Even a single character change will cause the signature to not match, signaling potential tampering.
- **Authenticity**: Confirms that the JWT was indeed issued by a trusted source and not by an unauthorized entity. This is achieved by signing the encoded header and payload.

### Step 1:

- **Create the Message to be Signed**: This involves combining the JWT's header and payload into a single string. Specifically:

  - **Header**: Metadata about the token, such as the type of token and the signing algorithm.
  - **Payload**: The data or claims being transmitted.

  These components are Base64Url encoded and concatenated with a dot (`.`) separator to form the "message" to be signed.

   ````json
   Header: {"alg": "HS256", "typ": "JWT"}
   Payload: {"sub": "1234567890", "name": "John Doe", "iat": 1516239022}
  ````

### Step 2

The Base64Url encoded header and payload might look like:

```JWT
Header Encoded: eyJhbGciOiAiSFMyNTYiLCAidHlwIjoiSldUIn0
Payload Encoded: eyJzdWIiOiAiMTIzNDU2Nzg5MCIsICJuYW1lIjogIkpvaG4gRG9lIiwgImlhdCI6IDE1MTYyMzkwMjJ9
````
Combine these encoded values:

```TEXT
Message to be Signed:
eyJhbGciOiAiSFMyNTYiLCAidHlwIjoiSldUIn0
.
eyJzdWIiOiAiMTIzNDU2Nzg5MCIsICJuYW1lIjogIkpvaG4gRG9lIiwgImlhdCI6IDE1MTYyMzkwMjJ9

```
### Step 3: Generate the Signature

- **Algorithm**: Use a signing algorithm (e.g., HMAC SHA256, RSA) specified in the JWT's header.
- **Secret Key**:
  - For symmetric algorithms like HMAC, a shared secret key is used.
  - For asymmetric algorithms like RSA, a private key is used.

##### HMAC SHA256 Signing Process

- Apply the SHA256 hash function to the combined header and payload message, using the secret key.
- The result is a hashed value that serves as the signature.

##### Example pseudocode for HMAC SHA256 signing

````TEXT
Signature = HMAC-SHA256(EncodedHeader + "." + EncodedPayload, SecretKey)
````

### Step 4: Attach the Signature

Concatenate the encoded header, encoded payload, and the signature, separated by dots (`.`),
to form the final JWT.

#### The final JWS (JSON Web Signature)

```JWT
eyJhbGciOiAiSFMyNTYiLCAic3MiOiAiSGVsbG8ifQ
.
eyJ1c2VyX2lkIjoiMTIzNDU2IiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNjI4MTUyMTIyfQ
.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

So, signing a JWT essentially means generating a cryptographic hash of the header and payload using a secret key. This
process ensures the integrity and authenticity of the JWT, as the signature can be verified by reconstructing the hash
on the recipient’s side with the same key and comparing it to the provided signature. If they match, the JWT is
validated; if not, it may have been tampered with or signed with a different key.
