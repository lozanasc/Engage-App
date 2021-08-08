const db_conn = require('../../database/db');

module.exports = async(name, desc, image) => {
    try {
        const query = `INSERT INTO project 
                        (
                            id, 
                            name, 
                            description, 
                            status,
                            image
                        )
                        VALUES
                        (
                            null,
                            '${name}',
                            '${desc}',
                            1,
                            '${image}'
                        )`;
        await db_conn(query);
        return true;
    } catch (error) {
        console.trace(error);
        return false;
    }
}