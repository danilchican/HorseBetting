$(document).ready(function () {

    var modal = $('#placeBetModal');
    var placeBetBtn = $('#place-bet-btn');

    var inputJockey = $('input#jockey');
    var inputCoefficient = $('input#coefficient');

    var amount = $('#bet-amount');
    var estimated = $('#estimated-returns');

    var coefficient;

    $('.place-bet').click(function () {
        var jockey = $(this).data('jockey');
        coefficient = $(this).data('coefficient');

        inputCoefficient.val(coefficient);
        inputJockey.val(jockey);

        estimated.text((amount.val() * coefficient).toFixed(2) + "$");

        modal.modal('show');
    });

    amount.on('change', function () {
        var am = amount.val();
        var result = (am * coefficient).toFixed(2);

        estimated.text(result + "$");
    });

    placeBetBtn.click(function () {
        $('#messages').empty();

        $.post('/ajax/profile/bets/create', {
            race_id: $("input[name='race-id']").val(),
            amount: $("input[name='amount']").val()
        })
            .done(function (data) {
                // success callback
                console.log("data betsCreate:");
                console.log(data);

                if (data.success === true) {
                    if (data.message !== undefined) {
                        alert(data.message);
                    }

                    modal.modal('hide');
                } else {
                    if (data.errors !== undefined) {
                        var htmlErrors = '<div class="alert alert-danger"><ul>';
                        $.each(data.errors, function (key, value) {
                            htmlErrors += '<li>' + value + '</li>';
                        });
                        htmlErrors += '</ul>';
                        $('#messages').html(htmlErrors);
                    } else {
                        alert('Что-то пошло не так...');
                    }
                }
            })
            .fail(function (data) {
                console.log("fail");
                if (data.errors !== undefined) {
                    // error callback
                    var htmlErrors = '<ul class="alert alert-danger">';
                    $.each(data.errors, function (key, value) {
                        htmlErrors += '<li>' + value + '</li>';
                    });
                    htmlErrors += '</ul></div>';
                    $('#messages').html(htmlErrors);
                }
            });

    });
});