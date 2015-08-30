(function(){
    var editor;

    function loadConfig(id) {
      $.getJSON('/configs/' + id, function(config) {
        $('#contentType').val(config.contentType);
        $('#accessToken').val(config.accessToken);
        editor.setValue(config.script);
      });
    }

    function getApiAccessToken() {
      return localStorage.getItem('accessToken');
    }

    function setApiAccessToken(token) {
        localStorage.setItem('accessToken', token);
    }

    $.ajaxSetup({
      beforeSend: function(req) {
        req.setRequestHeader('Authorization', 'token ' + getApiAccessToken());
      }
    });

    $(document).ready(function() {
      editor = ace.edit("editor");
      editor.setTheme("ace/theme/tomorrow_night");
      editor.getSession().setMode("ace/mode/javascript");

      var previewOutput = ace.edit("previewOutput");
      previewOutput.setTheme("ace/theme/tomorrow_night");

      loadConfig($('#id').val());

      $('#apiAccessToken').val(getApiAccessToken()).on('input', function() {
        setApiAccessToken(this.value);
      });

      $('#configurator').on('submit', function(e) {
        e.preventDefault();

        var id = $('#id').val();
        var contentType = $('#contentType').val();
        var accessToken = $('#accessToken').val();
        var code = editor.getValue();

        var json = {script: code, contentType: contentType, accessToken: accessToken};

        var method = id.length == 0 ? 'POST' : 'PUT';
        var url = method == 'POST' ? '/configs' : '/configs/' + id;

        $.ajax({
          type: method,
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          url: url,
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

      $('#preview').on('click', function(e) {
        e.preventDefault();
        previewOutput.setValue('');

        var contentType = $('#contentType').val();
        var code = editor.getValue();

        var json = {script: code, contentType: contentType };

        $.ajax({
          type: 'POST',
          dataType: 'json',
          contentType: 'application/json; charset=utf-8',
          url: '/endpointPreview',
          data: JSON.stringify(json),
          dataType: 'text',
          success: function(data) {
            previewOutput.getSession().setMode("ace/mode/xml");
            previewOutput.setValue(data);
            previewOutput.scrollToLine(0);

            $('#previewOutput, #editor').css({
              'display': 'block',
              'width': '49%',
              'float': 'left',
              'margin-right': '0.5%'
             });
          }
        });
      });
    });
})();
