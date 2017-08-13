$(document).ready(function () {

    var modal = $('#placeBetModal');
    var placeBetBtn = $('#place-bet-btn');

    var inputJockey = $('input#jockey');
    var inputCoefficient = $('input#coefficient');
    var inputParticipant = $('input#participant');

    var amount = $('#bet-amount');
    var estimated = $('#estimated-returns');

    var coefficient;

    $('.place-bet').click(function () {
        var jockey = $(this).data('jockey');
        coefficient = $(this).data('coefficient');
        var participant_id = $(this).data('participant');

        console.log("part: " + participant_id);

        inputCoefficient.val(coefficient);
        inputJockey.val(jockey);
        inputParticipant.val(participant_id);

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

        var data = {
            amount: $("input[name='amount']").val(),
            participant_id: $("input[name='participant']").val(),
        };

        $.post('/ajax/bets/place', data)
            .done(function (data) {
                // success callback
                console.log("data createBet:");
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
            .fail(function (response) {
                var data = response.responseJSON;

                console.log(data);
                if (data.errors !== undefined) {
                    var htmlErrors = '<div class="alert alert-danger">';
                    htmlErrors += data.errors;
                    htmlErrors += '</div>';
                    $('#messages').html(htmlErrors);
                } else {
                    alert('Ошибка сервера. Попробуйте позже.');
                }
            });

    });
});