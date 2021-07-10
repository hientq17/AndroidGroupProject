'use strict'

const responseBaseModel = require("../models/response-base-model")
const storeProcedureName = require("../store_procedures/store-procedure-name")
const isValidModel = require("../utils/model-filter") 

const apiMessage= function (dbConnection) {
    return {
        getListMessagesByInbox: (req, res) => {
            dbConnection.query(storeProcedureName().getListMessagesByInbox(),[req.query.inbox], (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        res.json("Not found")
                }
            })
        },
        getMessageById: (req, res) => {
            if (req.query.id > 0) {
                dbConnection.query(storeProcedureName().getMessageById(), [req.query.id], (err, response) => {
                    if (err) res.json(err);
                    if (response[0].length == 0)
                        res.json("Not found")
                    else
                        res.json(response[0])
                })
            }
            else {
                res.json("Not found")
            }
        },
        insertOrUpdateMessage: (req, res) => {
            console.log(req.body);
            if (isValidModel(req.body)) {
                let data = req.body;
                dbConnection.query(storeProcedureName().insertOrUpdateMessage(), [JSON.stringify(data.JInput),data.Action], (err, response) => {
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
        deleteMessage: (req, res) => {
            if (req.query.id > 0 ) {
                dbConnection.query(storeProcedureName().deleteMessage(), [req.query.id], (err, response) => {
                    let resData = response[0]
                    let returnId = resData[0].returnId
                    let outputMessage = resData[0].outputMessage
                    let model = new responseBaseModel(returnId, outputMessage=="SUCCESS"?true:false, outputMessage)
                    if (err) res.json(err)
                    res.json(model)
                })
            }
            else {
                res.json("Not found")
            }
        }
    }
}

module.exports = apiMessage