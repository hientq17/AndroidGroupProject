const express = require('express')
const app = express()
const bodyParser = require('body-parser')
const dbConnection = require('./db-config')
const port = process.env.PORT || 3000
const routes = require('./routers/router')
const authen = require('./authentication/authen')
const fileUpload = require('express-fileupload')

require('dotenv').load()

app.use(fileUpload())
app.use(bodyParser.json({limit: '50mb'}))

app.use(authen, (req, res, next) => {next()})

routes(app, dbConnection);

app.use(function (req, res) {
    res.status(404).send({ url: req.originalUrl + ' not found' })
})

app.listen(port)

console.log('Restful API server started on: ' + port)
