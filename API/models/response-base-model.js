function ResponseBaseModel(returnId, success, message) {
    this.returnId = returnId || null;
    this.success = success || null;
    this.message = message || null;
}

ResponseBaseModel.prototype.getReturnId = function () {
    return this.returnId;
}

ResponseBaseModel.prototype.setReturnId = function (returnId) {
    this.returnId = returnId;
}

ResponseBaseModel.prototype.getSuccess = function () {
    return this.success;
}

ResponseBaseModel.prototype.setSuccess = function (success) {
    this.success = success;
}

ResponseBaseModel.prototype.getMessage = function () {
    return this.message;
}

ResponseBaseModel.prototype.setMessage = function (message) {
    this.message = message;
}

ResponseBaseModel.prototype.equals = function (otherModel) {
    return otherModel.getMessage()  === this.getMessage()
        && otherModel.getSuccess()  === this.getSuccess()
        && otherModel.getReturnId() === this.getReturnId();
}

ResponseBaseModel.prototype.fill = function (newFields) {
    for (let field in newFields) {
        if (this.hasOwnProperty(field) && newFields.hasOwnProperty(field)) {
            if (this[field] !== 'undefined') {
                this[field] = newFields[field];
            }
        }
    }
};

module.exports = ResponseBaseModel;