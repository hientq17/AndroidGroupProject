'use strict'

const isValidModel = function (data) {
    let check = true
    if (JSON.stringify(data) === "{}") {
        check = false
    } else {
        let keys = []
        let args = typeof arguments[1]
        
        if (args === 'undefined') {
            keys = Object.keys(data)
        } else {
            keys = Array.prototype.slice.call(arguments, 1)
        }

        for (let i = 0; i <= keys.length; i++) {
            let el = "data." + keys[i]
            let value = eval(el)
            if (value !== 'undefined')
                if (value === null || value === "") {
                    check = false;
                    break;
                }
        }
    }

    return check;
}

module.exports = isValidModel