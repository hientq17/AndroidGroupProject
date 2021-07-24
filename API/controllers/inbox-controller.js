'use strict'

const responseBaseModel = require("../models/response-base-model")
const storeProcedureName = require("../configs/store-procedure-name")
const isValidModel = require("../utils/model-filter") 

const apiInbox= function (dbConnection) {
    return {
        getListInboxsByAuthor: (req, res) => {
            dbConnection.query(storeProcedureName().getListInboxsByAuthor(),[req.query.author], (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        res.json("EMPTY")
                }
            })
        },
        getInboxById: (req, res) => {
            if (req.query.id > 0) {
                dbConnection.query(storeProcedureName().getInboxById(), [req.query.id], (err, response) => {
                    if (err) res.json(err);
                    if (response[0].length == 0)
                        res.json("EMPTY")
                    else
                        res.json(response[0])
                })
            }
            else {
                res.json("EMPTY")
            }
        },
        insertOrUpdateInbox: (req, res) => {
            console.log(req.body);
            if (isValidModel(req.body)) {
                let data = req.body;
                dbConnection.query(storeProcedureName().insertOrUpdateInbox(), [JSON.stringify(data.JInput),data.Action], (err, response) => {
                    console.log(response);
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    if (err) res.json(err)
                    res.json(model)
                })
            }
            else {
                res.json("Invalid model")
            }
        },
        deleteInbox: (req, res) => {
            if (req.query.id > 0 ) {
                dbConnection.query(storeProcedureName().deleteInbox(), [req.query.id], (err, response) => {
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    if (err) res.json(err)
                    res.json(model)
                })
            }
            else {
                res.json("EMPTY")
            }
        }
    }
}

module.exports = apiInbox