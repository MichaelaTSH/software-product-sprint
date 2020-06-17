// https://codepen.io/AmruthPillai/pen/axvqB
(function ($) {
    $.fn.extend({
        rotaterator: function (options) {

            var defaults = {
                fadeSpeed: 500,
                pauseSpeed: 100,
                child: null
            };

            var options = $.extend(defaults, options);

            return this.each(function () {
                var o = options;
                var obj = $(this);
                var items = $(obj.children(), obj);
                items.each(function () {
                    $(this).hide();
                })
                if (!o.child) {
                    var next = $(obj).children(':first');
                } else {
                    var next = o.child;
                }
                $(next).fadeIn(o.fadeSpeed, function () {
                    $(next).delay(o.pauseSpeed).fadeOut(o.fadeSpeed, function () {
                        var next = $(this).next();
                        if (next.length == 0) {
                            next = $(obj).children(':first');
                        }
                        $(obj).rotaterator({
                            child: next,
                            fadeSpeed: o.fadeSpeed,
                            pauseSpeed: o.pauseSpeed
                        });
                    })
                });
            });
        }
    });
})(jQuery);

$(document).ready(function () {
    // Update year in copyright text
    document.querySelector('.tm-current-year').textContent = new Date().getFullYear();
});

var pictures = new Array("images/apollo.jpg", "images/bob.jpeg", "images/boomer.jpg", "images/drift.jpg",
                        "images/filbert.jpg", "images/kiki.jpg", "images/lolly.jpg", "images/punchy.jpg",
                        "images/rosie.jpg");

/**
 * Fetch the comment data in Json format and create a comment box in the comments section for each comment.
 */
async function fetchJson() {
    const response = await fetch('/data');
    const json = await response.json();

    var template = document.querySelector('#comment-template');
    var commentBox = template.content.querySelector('div');
    var temp;

    json.forEach(comment => {
        temp = modifyTemplate(commentBox, comment);
        document.querySelector('#comments').appendChild(temp);
    });
}

/**
 * Replace the icon and text of the template comment box with a random icon and the comment.
 */
function modifyTemplate(commentBox, comment) {
    const tempBox = document.importNode(commentBox, true);

    var randomNum = Math.floor(Math.random() * pictures.length);
    tempBox.querySelector('#comment-icon').src = pictures[randomNum];

    tempBox.querySelector('#comment-description').textContent = comment;

    return tempBox;
}
