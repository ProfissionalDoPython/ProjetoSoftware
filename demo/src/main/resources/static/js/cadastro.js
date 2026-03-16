const form = document.getElementById("registerForm");
const msg = document.getElementById("formMessage");
const limpar = document.getElementById("limpar");

function showError(text) {
  msg.style.display = "block";
  msg.textContent = text;
}
function clearError() {
  msg.style.display = "none";
  msg.textContent = "";
}

limpar.addEventListener("click", () => {
  form.reset();
  clearError();
});