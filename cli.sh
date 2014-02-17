#!/bin/bash

ACCEPT_JSON="Accept: application/json"
CONTENT_JSON="Content-Type: application/json"

JSON=""
HEADERS=""
LOGIN_COOKIE=""

METHOD="GET"
URL="http://localhost:8080/api"

case "$1" in
    users)
        case "$2" in
            list)
                HEADERS=${ACCEPT_JSON}
                ENDPOINT="users"
                ;;
            get)
                HEADERS=${ACCEPT_JSON}
                ENDPOINT="users/$3"
                ;;
            create)
                HEADERS=${CONTENT_JSON}
                ENDPOINT="users"
                METHOD="POST"
                JSON="{\"email\": \"${3}\", \"password\": \"${4}\", \"domain\": \"${5}\"}"
                ;;
            *)
                echo "usage: $0 users list"
                echo "       $0 users get    [id]"
                echo "       $0 users create [email] [password] [domain]"
                ;;
        esac
        ;;
    experiments)
        case "$2" in
            list)
                HEADERS=${ACCEPT_JSON}
                ENDPOINT="experiments"
                EMAIL=$3
                PASSWORD=$4
                ;;
            get)
                HEADERS=${ACCEPT_JSON}
                ENDPOINT="experiments/$3"
                EMAIL=$4
                PASSWORD=$5
                ;;
            *)
                echo "usage: $0 experiments list   [email] [password]"
                echo "       $0 experiments get    [id] [email] [password]"
                ;;
        esac
        ;;
    designs)
        case "$2" in
            list)
                HEADERS=${ACCEPT_JSON}
                ENDPOINT="experiments/$3/designs"
                EMAIL=$4
                PASSWORD=$5
                ;;
            get)
                HEADERS=${ACCEPT_JSON}
                ENDPOINT="experiments/$3/designs/$4"
                EMAIL=$5
                PASSWORD=$6
                ;;
            *)
                echo "usage: $0 designs list   [experiment] [email] [password]"
                echo "       $0 designs get    [experiment] [id] [email] [password]"
                ;;
        esac
        ;;
    algorithms)
        case "$2" in
            types)
                HEADERS=${ACCEPT_JSON}
                ENDPOINT="algorithms"
            ;;
            *)
                echo "usage: $0 algorithms types"
            ;;
        esac
        ;;
    rewards)
        case "$2" in
            add)
                HEADERS=${CONTENT_JSON}
                ENDPOINT="rewards/$3"
                METHOD="POST"
                JSON="{\"value\": \"${4}\"}"
                ;;
            *)
                echo "usage: $0 rewards add [design] [value]"
            ;;
        esac
        ;;
    *)
        echo "usage: $0 users"
        echo "       $0 experiments"
        echo "       $0 designs"
        echo "       $0 algorithms"
        ;;
esac

if [ -n "$EMAIL" ]; then
    JSON="{\"email\": \"${EMAIL}\", \"password\": \"${PASSWORD}\"}"
    LOGIN_COOKIE=$(curl -is -X POST -H "${CONTENT_JSON}" -d "${JSON}" "${URL}/auth/login" | awk -F'[; ]' '/Set-Cookie: / {print $2}')
fi

if [ -n "$ENDPOINT" ]; then
    curl -is -X "${METHOD}" -H "${HEADERS}" -d "${JSON}" --cookie "${LOGIN_COOKIE}" "${URL}/${ENDPOINT}"
fi

exit 0
