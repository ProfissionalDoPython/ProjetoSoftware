function login() {
  const senha = document.getElementById("senha").value;
  const confSenha = document.getElementById("confSenha").value;

  if (!senha || !confSenha) {
    alert("Por favor, preencha todos os campos de senha.");
  } else if (senha !== confSenha) {
    alert("As senhas não coincidem!");
  } else if (senha.length < 8) {
    alert("A senha deve ter no mínimo 8 caracteres.");
  } else {
    // This is where you would normally submit the form
    alert(`Nova senha definida com sucesso (em um cenário real).`);
  }
}