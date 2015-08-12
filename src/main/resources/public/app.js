var editor;

function loadConfig(id) {
  $.getJSON('/config/' + id, function(config) {
    $('#url').val(config.pageUrl);
    $('#contentType').val(config.contentType);
    editor.setValue(config.script);
  });
}

$(document).ready(function() {
  editor = ace.edit("editor");
  editor.setTheme("ace/theme/tomorrow_night");
  editor.getSession().setMode("ace/mode/javascript");

  loadConfig($('#id').val());

  $('#configurator').on('submit', function(e) {
    e.preventDefault();

    var id = $('#id').val();
    var url = $('#url').val();
    var contentType = $('#contentType').val();
    var code = editor.getValue();

    var json = {pageUrl: url, script: code, contentType: contentType };

    $.ajax({
      type: 'PUT',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      url: '/config/' + id,
      data: JSON.stringify(json),
      success: function() {
        $('#success').show();
        setTimeout(function() {
          $('#success').hide();
        }, 1000);
      }
    });
  });

  $('#loadConfig').on('click', function(e) {
    e.preventDefault();

    var id = $('#id').val();
    loadConfig(id);
  });
});
