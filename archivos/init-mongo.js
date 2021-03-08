db.createUser(
    {
        user: "johanrueda",
        pwd: "johanrueda",
        roles: [
            {
                role: "readWrite",
                db: "docker"
            }
        ]
    }
)