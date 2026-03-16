function validateAndProceed() {
  const email = document.getElementById("email").value;
  const confirmEmail = document.getElementById("confirmEmail").value;

  if (!email || !confirmEmail) {
    alert("Por favor, preencha os dois campos de e-mail.");
    return;
  }

  if (email !== confirmEmail) {
    alert("Os e-mails não coincidem. Por favor, verifique.");
    return;
  }

  // If validation passes, redirect to the next page.
  window.location.href = "telasenha.html";
}