const db_conn = require('../../database/db');

module.exports = async() => {
    try {
        // Fetches data that are currently active
        const data = await db_conn('SELECT * FROM project WHERE status = 1');
        return data;
    } catch (error) {
        return [];
        console.trace(error);
    }
}