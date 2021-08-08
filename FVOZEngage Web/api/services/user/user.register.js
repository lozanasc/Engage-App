const db_conn = require('../../database/db');

module.exports = async(email, name, contact, password, profile) => {
    try {
        const Query = `INSERT INTO user 
                        (
                            user_id, 
                            user_email,
                            user_name,
                            user_contact,
                            user_password,
                            user_profile
                        )
                        VALUES
                        (
                            null,
                            '${email}',
                            '${name}',
                            '${contact}',
                            '${password}',
                            '${profile}'
                        )
                        `;
        await db_conn(Query);
        return true;
    } catch (error) {
        console.trace(error);
        return false;
    }
}