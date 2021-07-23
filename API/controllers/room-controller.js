'use strict'

const responseBaseModel = require("../models/response-base-model")
const storeProcedureName = require("../store_procedures/store-procedure-name")
const isValidModel = require("../utils/model-filter")

const apiRoom= function (dbConnection) {
    return {
        getListAllRooms: (req,res) => {
            dbConnection.query(storeProcedureName().getListAllRooms(),null, (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        res.json("Not found")
                }
            })
        },
        getTopRooms: (req,res) => {
            dbConnection.query(storeProcedureName().getTopRooms(),null, (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        res.json("Not found")
                }
            })
        },
        getListRoomsByAuthor: (req, res) => {
            dbConnection.query(storeProcedureName().getListRoomsByAuthor(),[req.query.author], (err, response) => {
                if (err) res.json(err)
                else{
                    if(response[0].length > 0)
                         res.json(response[0])
                    else
                        res.json("Not found")
                }
            })
        },
        getRoomById: (req, res) => {
            if (req.query.id > 0) {
                dbConnection.query(storeProcedureName().getRoomById(), [req.query.id], (err, response) => {
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
        insertOrUpdateRoom: (req, res) => {
            if (isValidModel(req.body)) {
                let data = req.body;
                dbConnection.query(storeProcedureName().insertOrUpdateRoom(), [JSON.stringify(data.JInput),data.Action], (err, response) => {
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
        deleteRoom: (req, res) => {
            if (req.query.id > 0 ) {
                dbConnection.query(storeProcedureName().deleteRoom(), [req.query.id], (err, response) => {
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

module.exports = apiRoom