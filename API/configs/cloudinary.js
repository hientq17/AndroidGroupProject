const cloudinary = require('cloudinary').v2;
cloudinary.config({
    cloud_name: 'mrend',
    api_key: '935971468737829',
    api_secret: 'BqEH96l0EZ5rM7bh1BxRexNcscY'
});
module.exports = cloudinary;