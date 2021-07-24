'use strict'

const md5 = require('md5');
const jwt = require('jsonwebtoken')

let refreshTokens = []

const responseBaseModel = require("../models/response-base-model")
const storeProcedureName = require("../configs/store-procedure-name")
const isValidModel = require("../utils/model-filter")

const apiUser= function (dbConnection) {
    return {
        login: (req, res) => {
            let data = req.body;
            if (data.username != null && data.password != null) {  
                data.password = md5(data.password);
                dbConnection.query(storeProcedureName().login(), [JSON.stringify(data)], (err, response) => {
                    console.log(response);
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    let accessToken = null;
                    if (response[1].length >0){
                        accessToken = jwt.sign(JSON.parse(JSON.stringify(response[1]))[0], process.env.ACCESS_TOKEN_SECRET || "MySecretToken@123", { expiresIn: '2d' })
                    }
                    if (err) res.json(err);
                    else res.json({returnModel: model,token:accessToken})
                })
            }
        },
        signup: (req, res) => {
            let data = req.body;
            if (data.username != null && data.password != null) {
                data.password = md5(data.password);
                console.log(data);
                dbConnection.query(storeProcedureName().signup(), [JSON.stringify(data)], (err, response) => {
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    if (err) res.json(err);
                    else res.json(model)
                })
            }
        },
        getUserByUsername: (req, res) => {
            if (req.query.username != null) {
                dbConnection.query(storeProcedureName().getUserByUsername(), [req.query.username], (err, response) => {
                    if (err) res.json(err);
                    if (response[0].length == 0)
                        res.json("EMPTY")
                    else
                        res.json(response[0][0])
                })
            }
            else {
                res.json("EMPTY")
            }
        },
        updateUserInfo:(req, res) => {
            let data = req.body;
            console.log(data)
            if (data.username != null 
                && data.name != null
                && data.address != null
                && data.phone != null) {
                dbConnection.query(storeProcedureName().updateUserInfo(), [JSON.stringify(data)], (err, response) => {
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    console.log(model)
                    if (err) res.json(err);
                    else res.json(model)
                })
            }
        },
        changePassword:(req, res) => {
            let data = req.body;
            if (data.currentPassword != null && data.newPassword != null && data.username) {
                
                data.currentPassword = md5(data.currentPassword);
                data.newPassword = md5(data.newPassword);
                
                dbConnection.query(storeProcedureName().changePassword(), [JSON.stringify(data)], (err, response) => {
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    if (err) res.json(err);
                    else res.json(model)
                })
            }
        }
    }
}

module.exports = apiUser