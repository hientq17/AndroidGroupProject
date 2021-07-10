const express = require('express')
const app = express()
const bodyParser = require('body-parser')
const dbConnection = require('./db-config')
const port = process.env.PORT || 3000
const routes = require('./routers/router')
const authen = require('./authentication/authen')

require('dotenv').load()

app.use(bodyParser.json())

app.use(authen, (req, res, next) => {next()})

routes(app, dbConnection);

app.use(function (req, res) {
    res.status(404).send({ url: req.originalUrl + ' not found' })
})

app.listen(port)

console.log('Restful API server started on: ' + port)
