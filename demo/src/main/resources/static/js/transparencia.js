// Função global para alternar o tema (reusável e consistente)
function toggleTheme() {
  const body = document.body;
  body.classList.toggle("dark-mode"); // Toggles the 'dark-mode' class on the body
  
  // Update localStorage to remember the theme preference
  if (body.classList.contains("dark-mode")) {
    localStorage.setItem("theme", "dark");
  } else {
    localStorage.setItem("theme", "light");
  }
}

// Carrega o tema salvo do localStorage quando o DOM está completamente carregado
window.addEventListener("DOMContentLoaded", () => {
  const savedTheme = localStorage.getItem("theme");
  if (savedTheme === "dark") {
    document.body.classList.add("dark-mode"); // Apply 'dark-mode' class if saved preference is dark
  }
});

// Função para alternar o menu mobile
function toggleMenu() {
  const menu = document.querySelector('.menu-links');
  menu.classList.toggle('show');
}