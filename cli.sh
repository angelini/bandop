#!/bin/bash

ACCEPT_JSON="Accept: application/json"
CONTENT_JSON="Content-Type: application/json"

HEADERS=""
LOGIN_COOKIE=""

METHOD="GET"
URL="http://localhost:8080/api"

case "$1" in
    users)
        case "$2" in
            list)
                HEADERS=$ACCEPT_JSON
                ENDPOINT="users"
                ;;
            get)
                HEADERS=$ACCEPT_JSON
                ENDPOINT="users/$3"
                ;;
            *)
                echo "usage: $0 users list"
                echo "       $0 users get  [id]"
                ;;
        esac
        ;;
    designs)
        case "$2" in
            list)
                HEADERS=$ACCEPT_JSON
                ENDPOINT="designs"
                EMAIL=$3
                PASSWORD=$4
                ;;
            get)
                HEADERS=$ACCEPT_JSON
                ENDPOINT="designs/$3"
                EMAIL=$4
                PASSWORD=$5
                ;;
            *)
                echo "usage: $0 designs list [email] [password]"
                echo "       $0 designs get  [id] [email] [password]"
                ;;
        esac
        ;;
    *)
        echo "usage: $0 users"
        echo "       $0 designs"
        ;;
esac

if [ -n "$EMAIL" ]; then
    JSON="{\"email\": \"${EMAIL}\", \"password\": \"${PASSWORD}\"}"
    LOGIN_COOKIE=$(curl -is -X POST -H "${CONTENT_JSON}" -d "${JSON}" "${URL}/auth/login" | awk -F'[; ]' '/Set-Cookie: / {print $2}')
fi

if [ -n "$ENDPOINT" ]; then
    curl -is -X "${METHOD}" -H "${HEADERS}" --cookie "${LOGIN_COOKIE}" "${URL}/${ENDPOINT}"
fi

exit 0