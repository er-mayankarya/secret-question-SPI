<html>
  <body>
    <h3>Answer your secret question</h3>
    <form action="${url.loginAction}" method="post">
      <div>
        <label>Secret Question:</label>
        <p>${user.attributes['secret_question'][0]!''}</p>
      </div>
      <div>
        <label>Answer:</label>
        <input type="text" name="secret_answer" />
      </div>
      <div>
        <input type="submit" value="Submit" />
      </div>
    </form>
  </body>
</html>

