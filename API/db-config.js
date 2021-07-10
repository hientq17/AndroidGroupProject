'use strict';
const mysql = require('mysql');

const dbConfig = {
  connectionLimit: process.env.DB_LIMIT || 100,
  host: process.env.DB_HOST || "103.97.125.251",
  user: process.env.DB_USER || "mrendzpc_nodejs",
  password: process.env.DB_PASSWORD || "Nodejs13579",
  database: process.env.DB_DATABASE || "mrendzpc_FTro"
};

const dbConnection = mysql.createPool(dbConfig);

module.exports = dbConnection