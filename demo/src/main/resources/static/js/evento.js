// Function to handle theme toggling
function toggleTheme() {
  document.body.classList.toggle("dark-mode");
  const theme = document.body.classList.contains("dark-mode") ? "dark" : "light";
  localStorage.setItem("theme", theme);
}

// Function to toggle the mobile menu
function toggleMenu() {
  const menu = document.querySelector('.menu-links');
  menu.classList.toggle('show');
}

// --- Funções para carregar e exibir eventos ---

async function fetchAndDisplayEvents() {
    const destaqueContainer = document.getElementById('destaque-evento-container');
    const todosEventosLista = document.getElementById('todos-eventos-lista');

    // Limpa o conteúdo inicial
    destaqueContainer.innerHTML = '<p>Carregando evento destaque...</p>';
    todosEventosLista.innerHTML = '<p>Carregando eventos...</p>';

    try {
        const response = await fetch('http://localhost:8080/api/eventos/todos'); // Endpoint para todos os eventos
        
        if (!response.ok) {
            if (response.status === 401) {
                window.location.href = '/login'; // Redireciona se não estiver logado
                return;
            }
            throw new Error(`Erro ao carregar eventos: Status ${response.status}`);
        }

        const data = await response.json();

        if (data.sucesso && Array.isArray(data.eventos)) {
            const eventos = data.eventos;
            
            // Exibir evento destaque
            const eventoDestaque = eventos.find(e => e.destaque);
            if (eventoDestaque) {
                renderEventoDestaque(eventoDestaque, destaqueContainer);
            } else {
                destaqueContainer.innerHTML = '<p>Nenhum evento em destaque no momento.</p>';
            }

            // Exibir todos os eventos na lista
            renderTodosEventos(eventos, todosEventosLista);

        } else {
            console.error('Erro na resposta da API de eventos:', data.erro || 'Formato de dados inválido.');
            destaqueContainer.innerHTML = '<p class="mensagem-erro">Erro ao carregar evento destaque.</p>';
            todosEventosLista.innerHTML = '<p class="mensagem-erro">Erro ao carregar eventos.</p>';
        }

    } catch (error) {
        console.error('Erro na requisição da API de eventos:', error);
        destaqueContainer.innerHTML = '<p class="mensagem-erro">Não foi possível conectar ao servidor de eventos.</p>';
        todosEventosLista.innerHTML = '<p class="mensagem-erro">Não foi possível conectar ao servidor de eventos.</p>';
    }
}

/**
 * Renderiza um evento em destaque no container especificado.
 * @param {Object} evento O objeto evento a ser renderizado.
 * @param {HTMLElement} container O elemento HTML onde o evento será inserido.
 */
function renderEventoDestaque(evento, container) {
    container.innerHTML = `
        <h3>${evento.titulo}</h3>
        <ul class="detalhes-evento">
            <li><strong>Data:</strong> ${evento.data}</li>
            <li><strong>Local:</strong> ${evento.local}</li>
        </ul>
        <p class="descricao">${evento.descricao}</p>
        <a href="${evento.linkInscricao}" class="botao-link" target="_blank">Saiba mais e inscreva-se</a>
    `;
}

/**
 * Renderiza uma lista de eventos no container especificado.
 * @param {Array<Object>} eventos A lista de objetos evento a serem renderizados.
 * @param {HTMLElement} container O elemento HTML (UL) onde os eventos serão inseridos.
 */
function renderTodosEventos(eventos, container) {
    container.innerHTML = ''; // Limpa a lista existente

    if (eventos.length === 0) {
        container.innerHTML = '<li>Nenhum evento disponível no momento.</li>';
        return;
    }

    eventos.forEach(evento => {
        const li = document.createElement('li');
        li.innerHTML = `
            <strong>${evento.titulo}</strong> - ${evento.data} - Local: ${evento.local}<br/>
            <span class="descricao">${evento.descricao}</span>
            ${evento.linkInscricao ? `<br/><a href="${evento.linkInscricao}" class="botao-link" target="_blank">Inscrição</a>` : ''}
        `;
        container.appendChild(li);
    });
}


// --- Event Listeners para a página ---

// Garante que o tema é carregado e os eventos são buscados quando o DOM está pronto.
window.addEventListener("DOMContentLoaded", () => {
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme === "dark") {
        document.body.classList.add("dark-mode");
    }
    fetchAndDisplayEvents(); // Chama a função para carregar e exibir eventos
});