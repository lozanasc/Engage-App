const db_conn = require('../../database/db');

module.exports = async(project_id) => {
    try {
        await db_conn(`UPDATE project SET status = 0 WHERE id = ${project_id}`);
        return true;
    } catch (error) {
        console.trace(error);
        return false;
    }
}