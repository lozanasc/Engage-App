const db_conn = require('../../database/db');

module.exports = async(id, name, desc, image) => {
    try {
        const query = `UPDATE project SET name = '${name}', description = "${desc}", image = '${image}' WHERE id = ${id}`;
        await db_conn(query);
        return true;
    } catch (error) {
        console.trace(error);
        return false;
    }
}