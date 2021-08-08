const mysql = require('mysql2');

const Configuration = {
    host: process.env.DB_CONNECTION_HOST,
    user: process.env.DB_CONNECTION_USER,
    password: process.env.DB_CONNECTION_PASSWORD,
    database: process.env.DB_NAME,
    multipleStatements: true
}

const Connection = mysql.createPool(Configuration);

module.exports = (Query) => {
    return new Promise ((Resolve, Reject) => {
        try {
            Connection.getConnection((Error, AttempConnect) => {
                Error ?
                Reject(Error)
                :
                AttempConnect.query(Query, (Error, Rows) => {
                    Error ?
                    console.trace('Something went wrong:', Error)
                    :
                    Resolve(Rows);
                    AttempConnect.release();
                })
            })
        } catch (error) {
            console.trace(error);
        }
    })
}
