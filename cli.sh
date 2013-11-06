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
    LOGIN_JSON="'{\"email\": \"${EMAIL}\", \"password\": \"${PASSWORD}\"}'"
    LOGIN_COMMAND="curl -is -X POST -H \"${CONTENT_JSON}\" -d ${LOGIN_JSON} ${URL}/auth/login | awk -F'[; ]' '/Set-Cookie: / {print \$2}' $LOGIN_INFO"

    LOGIN_COOKIE=`eval $LOGIN_COMMAND`
fi

if [ -n "$ENDPOINT" ]; then
    COMMAND="curl -is -X \"${METHOD}\" -H \"${HEADERS}\" --cookie \"${LOGIN_COOKIE}\" $URL/$ENDPOINT"
    echo "$COMMAND"
    echo ""
    eval $COMMAND
fi

exit 0
