'use-strict'
const jwt = require('jsonwebtoken')
const verifiedUrl = [
    "/api/user/get-user-by-username",
    "/api/room/get-list-rooms-by-author",
    "/api/room/insert-or-update-room",
    "/api/room/delete-room",
    "/api/message/get-list-messages-by-inbox",
    "/api/message/get-message-by-id",
    "/api/message/insert-or-update-message",
    "/api/message/delete-message",
    "/api/inbox/get-list-inboxs-by-author",
    "/api/inbox/get-inbox-by-id",
    "/api/inbox/insert-or-update-inbox",
    "/api/inbox/delete-inbox",
    "/api/book/get-list-books-by-room",
    "/api/book/get-book-by-id",
    "/api/book/insert-or-update-book",
    "/api/book/delete-book"
]
const jwtAuthentication = function authen(req, res, next) {
    if(verifiedAuthen(req)){
        let token = req.query.token
        if (token == null) return res.sendStatus(401)
        jwt.verify(token, process.env.ACCESS_TOKEN_SECRET || "MySecretToken@123", (err, user) => {
            console.log(err)
            if (err) return res.sendStatus(403)
            next()
        })
    } else {
        next();
    }
    
}

const verifiedAuthen = function verifiedAuthen(req){
    let stt = false;
    verifiedUrl.forEach(url => {
        if(req.url.includes(url)) {
            stt = true;
            return true;
        }
    })
    return stt;
}
module.exports = jwtAuthentication