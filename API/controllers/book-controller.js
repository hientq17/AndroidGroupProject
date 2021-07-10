'use strict'

const responseBaseModel = require("../models/response-base-model")
const storeProcedureName = require("../store_procedures/store-procedure-name")
const isValidModel = require("../utils/model-filter") 

const apiBook= function (dbConnection) {
    return {
        getListBooksByRoom: (req, res) => {
            dbConnection.query(storeProcedureName().getListBooksByRoom(),[req.query.room], (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        res.json("Not found")
                }
            })
        },
        getBookById: (req, res) => {
            if (req.query.id > 0) {
                dbConnection.query(storeProcedureName().getBookById(), [req.query.id], (err, response) => {
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
        insertOrUpdateBook: (req, res) => {
            console.log(req.body);
            if (isValidModel(req.body)) {
                let data = req.body;
                dbConnection.query(storeProcedureName().insertOrUpdateBook(), [JSON.stringify(data.JInput),data.Action], (err, response) => {
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
        deleteBook: (req, res) => {
            if (req.query.id > 0 ) {
                dbConnection.query(storeProcedureName().deleteBook(), [req.query.id], (err, response) => {
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

module.exports = apiBook