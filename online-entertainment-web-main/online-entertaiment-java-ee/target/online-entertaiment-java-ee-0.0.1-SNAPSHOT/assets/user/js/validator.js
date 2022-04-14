// Đối tượng `Validator`
function Validator(options) {
    var selectorRules = {};

    function getParent(element, selector) {
        while (element.parentElement) {
            if (element.parentElement.matches(selector)) {
                return element.parentElement;
            }
            element = element.parentElement;
        }
    }

    // Hàm thực hiện validate
    function validate(inputElement, rule) {

        var errorElement = getParent(inputElement, options.formGroupSelector).querySelector(options.errorSelector);
        var errorMessage;

        // Lấy ra các rule của selector
        var rules = selectorRules[rule.selector];

        // Lặp qua từng rule & kiểm tra
        // Nếu có lỗi thì dừng việc kiểm tra
        for (var i = 0; i < rules.length; i++) {
            switch (inputElement.type) {
                case 'radio':
                case 'checkbox':
                    errorMessage = rules[i](
                        formElement.querySelector(rule.selector + ':checked')
                    );
                    break;
                default:
                    errorMessage = rules[i](inputElement.value);
            }
            if (errorMessage) break;
        }

        switch (inputElement.type) {
            case 'radio':
            case 'checkbox':
                if (errorMessage) {
                    errorElement.innerText = errorMessage;
                } else {
                    errorElement.innerText = "";
                }
                break;
            default:
                if (errorMessage) {
                    errorElement.innerText = errorMessage;
                    inputElement.classList.add('is-invalid');
                    // getParent(inputElement, options.formGroupSelector).classList.add('invalid');
                } else {
                    errorElement.innerText = "";
                    inputElement.classList.remove('is-invalid');
                    // getParent(inputElement, options.formGroupSelector).classList.add('invalid');
                }
        }
        return !errorMessage;
    }

    // Lấy element của form cần validate
    var formElement = document.querySelector(options.form);
    if (formElement) {
        //Khi submit form
        formElement.onsubmit = function (e) {
            e.preventDefault();

            var isFormValid = true;

            options.rules.forEach(function (rule) {
                var inputElement = formElement.querySelector(rule.selector);
                var isValid = validate(inputElement, rule);
                if (!isValid) {
                    isFormValid = false;
                }
            });

            if (isFormValid) {
                // Trường hợp submit với JS
                if (typeof options.onSubmit === 'function') {
                    var enableInputs = formElement.querySelectorAll('[name]:not([disable])');
                    var formValues = Array.from(enableInputs).reduce(function (values, input) {
                        switch (input.type) {
                            case 'radio':
                                values[input.name] = formElement.querySelector('input[name="' + input.name + '"]:checked').value;
                                break;
                            case 'checkbox':
                                if (!input.matches(':checked')) {
                                    return values;
                                };

                                if (!Array.isArray(values[input.name])) {
                                    values[input.name] = [];
                                }

                                values[input.name].push(input.value);

                                break;
                            case 'file':
                                values[input.name] = input.files;
                                break;
                            default:
                                values[input.name] = input.value;
                        }
                        return values;
                    }, {});
                    // Gọi lại hàm onSubmit và trả về kèm giá trị của form
                    options.onSubmit(formValues);
                }
                // Trường hợp submit với hành vi mặc định
                else {
                    formElement.submit();
                }
            }

        }

        //Lặp qua mỗi rule và xử lý (lắng nghe sự kiện blur, input,...)
        options.rules.forEach(function (rule) {
            // Lưu lại các rules trong mỗi input
            if (Array.isArray(selectorRules[rule.selector])) {
                selectorRules[rule.selector].push(rule.test);
            } else {
                selectorRules[rule.selector] = [rule.test];
            }

            var inputElements = formElement.querySelectorAll(rule.selector);

            Array.from(inputElements).forEach(function (inputElement) {
                if (inputElement) {
                    // Xử lý trường hợp blur khỏi input
                    inputElement.onblur = function () {
                        validate(inputElement, rule);
                    }

                    // Xử lý mỗi khi người dùng nhập vào input
                    inputElement.oninput = function () {
                        var errorElement = getParent(inputElement, options.formGroupSelector).querySelector('.form-message');
                        errorElement.innerText = "";
                        inputElement.classList.remove('is-invalid');
                        // getParent(inputElement, options.formGroupSelector).classList.add('invalid');
                    }
                }
            });
        });
    }
}

// Định nghĩa các rules
Validator.isRequired = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            return value.trim() ? undefined : message || 'Vui lòng nhập vào trường này!'
        }
    }

}

Validator.isRequiredCheckbox = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            return value ? undefined : message || 'Vui lòng đánh dấu ít nhất một lựa chọn!'
        }
    }
}

Validator.isRequiredFile = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            return value ? undefined : message || 'Vui lòng chọn file!'
        }
    }

}

Validator.isRequiredRadio = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            return value ? undefined : message || 'Vui lòng đánh dấu một lựa chọn!'
        }
    }

}

Validator.isRequiredSelect = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            return value ? undefined : message || 'Vui lòng chọn một mục trong danh sách!'
        }
    }

}

Validator.isEmail = function (selector, message) {
    return {
        selector: selector,
        test: function (value) {
            var regexEmail = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
            return regexEmail.test(value) ? undefined : message || 'Email không hợp lệ!'
        }
    }
}

Validator.minLength = function (selector, min, message) {
    return {
        selector: selector,
        test: function (value) {
            return value.length >= min ? undefined : message || `Vui lòng nhập tổi thiểu ${min} ký tự!`
        }
    }
}

Validator.maxLength = function (selector, max, message) {
    return {
        selector: selector,
        test: function (value) {
            return value.length <= max ? undefined : message || `Vui lòng nhập tổi thiểu ${max} ký tự!`
        }
    }
}

Validator.isConfirmed = function (selector, getConfirmValue, message) {
    return {
        selector: selector,
        test: function (value) {
            return value === getConfirmValue() ? undefined : message || 'Giá trị nhập vào không chính xác!'
        }
    }
}

// Use example:
{
    /* <script>
        Validator({
            form: '#register-form',
            formGroupSelector: '.form-group',
            errorSelector: '.form-message',
            rules: [
                Validator.isRequired('#username'),
                Validator.isRequired('#fullname'),
                Validator.isRequired('#email'),
                Validator.isEmail('#email'),
                Validator.minLength('#password', 6, 'Mật khẩu phải tối thiểu 6 ký tự!'),
                Validator.isRequired('#confirm-password'),
                Validator.isConfirmed(
                    '#confirm-password',
                    function () {
                        return document.querySelector('#register-form #password').value;
                    },
                    'Mật khẩu xác nhận không chính xác!'
                ),
                Validator.isRequiredRadio('input[name="gender"]'),
                Validator.isRequiredRadio('#province'),
                Validator.isRequiredFile('#formFile'),
            ],
            onSubmit: function (data) {
                console.log(data);
            }
        });
    </script> */
}