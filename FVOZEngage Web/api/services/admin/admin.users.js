const db_conn = require('../../database/db');

module.exports = async() => {
	try {
		const result = await db_conn('SELECT * FROM user');
		return result;
	} catch (error) {
		console.trace(error);
		return [];
	}

}

