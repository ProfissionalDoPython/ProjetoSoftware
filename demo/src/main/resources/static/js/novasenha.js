function toggleTheme() {
  const html = document.documentElement;
  const isDark = html.getAttribute("data-theme") === "dark";
  html.setAttribute("data-theme", isDark ? "light" : "dark");
}
document.getElementById("form-senha").addEventListener("submit", function(e) {
    e.preventDefault(); // Impede envio automático

    const senha = document.getElementById("nova_senha").value;
    const confirmar = document.getElementById("confirmar_senha").value;
    const erroDiv = document.getElementById("erro-senha");

    if (senha !== confirmar) {
        erroDiv.textContent = "As senhas não coincidem. Por favor, verifique.";
        erroDiv.style.display = "block";
    } else if (senha.length < 6) {
        erroDiv.textContent = "A senha deve ter pelo menos 6 caracteres.";
        erroDiv.style.display = "block";
    } else {
        erroDiv.style.display = "none";

        // Simula envio bem-sucedido
        const sucessoDiv = document.createElement("div");
        sucessoDiv.className = "flash-message";
        sucessoDiv.textContent = "Senha alterada com sucesso!";
        document.querySelector(".container").prepend(sucessoDiv);

        // Redireciona após 2 segundos
        setTimeout(() => {
            window.location.href = "login1.html";
        }, 2000);
    }
});
