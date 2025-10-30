document.addEventListener('DOMContentLoaded', () => {
    const container = document.getElementById('cards-container');
    const dados = [
      { id: 1, titulo: "Processo 1" },
      { id: 2, titulo: "Processo 2" }
    ];
  
    dados.forEach(item => {
      const card = document.createElement('div');
      card.className = 'card';
      card.innerHTML = `
        <div class="imagem">foto</div>
        <button onclick="window.location.href='processo.html'">Saiba Mais</button>
      `;
      container.appendChild(card);
    });
  });
  