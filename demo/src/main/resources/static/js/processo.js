// Function to toggle the mobile menu (consistent with other pages)
function toggleMenu() {
    const menu = document.querySelector('.menu-links');
    menu.classList.toggle('show');
}

// Function to handle theme toggling (consistent with other pages)
function toggleTheme() {
    document.body.classList.toggle("dark-mode");
    const theme = document.body.classList.contains("dark-mode") ? "dark" : "light";
    localStorage.setItem("theme", theme);
}

document.addEventListener("DOMContentLoaded", () => {
    // Apply saved theme on load
    const savedTheme = localStorage.getItem("theme");
    if (savedTheme === "dark") {
        document.body.classList.add("dark-mode");
    }

    const filtroArea = document.getElementById("filtro-area");
    const filtroModalidade = document.getElementById("filtro-modalidade");
    const filtroMes = document.getElementById("filtro-mes");
    const processosContainer = document.getElementById("processos-container"); // Where process cards will be loaded
    const btnLimpar = document.getElementById("btn-limpar");

    let allProcessos = []; // To store all processes fetched from the API

    // Function to fetch and display processes
    async function fetchAndDisplayProcessos() {
        processosContainer.innerHTML = '<p class="text-gray-600 text-center">Carregando processos seletivos...</p>'; // Loading feedback

        try {
            // Fetch from your backend API endpoint for processes
            const response = await fetch('http://localhost:8080/api/processos/todos'); 
            
            if (!response.ok) {
                if (response.status === 401) {
                    window.location.href = '/login'; // Redirect if not authenticated
                    return;
                }
                throw new Error(`Erro ao carregar processos: Status ${response.status}`);
            }

            const data = await response.json();

            if (data.sucesso && Array.isArray(data.processos)) {
                allProcessos = data.processos; // Store all fetched processes
                aplicarFiltros(); // Apply initial filters (show all)
            } else {
                console.error('Erro na resposta da API de processos:', data.erro || 'Formato de dados inválido.');
                processosContainer.innerHTML = '<p class="text-red-500 text-center">Erro ao carregar processos seletivos.</p>';
            }

        } catch (error) {
            console.error('Erro na requisição da API de processos:', error);
            processosContainer.innerHTML = '<p class="text-red-500 text-center">Não foi possível conectar ao servidor de processos.</p>';
        }
    }

    // Function to render a single process card
    function renderProcessoCard(processo) {
        return `
            <div
                class="w-full md:w-1/2 p-4" >
                <div class="border rounded-xl p-4 shadow bg-white">
                    <h2 class="text-xl font-semibold">${processo.titulo}</h2>
                    <p class="text-gray-600">📅 Inscrições até ${processo.inscricoesAte}</p>
                    <p class="mt-2">${processo.etapas}</p> <br>
                    <a
                        href="${processo.linkCandidatura}"
                        class="mt-4 bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
                        target="_blank" >
                        Candidatar-se
                    </a>
                </div>
            </div>
        `;
    }

    // Function to apply filters and render processes
    function aplicarFiltros() {
        const area = filtroArea.value;
        const modalidade = filtroModalidade.value;
        const mes = filtroMes.value;

        processosContainer.innerHTML = ''; // Clear container before rendering

        const processosFiltrados = allProcessos.filter((processo) => {
            const matchArea = !area || (processo.area && processo.area.toLowerCase() === area.toLowerCase());
            const matchModalidade = !modalidade || (processo.modalidade && processo.modalidade.toLowerCase() === modalidade.toLowerCase());
            const matchMes = !mes || (processo.mes && processo.mes.toLowerCase() === mes.toLowerCase());
            return matchArea && matchModalidade && matchMes;
        });

        if (processosFiltrados.length > 0) {
            processosFiltrados.forEach((processo) => {
                processosContainer.insertAdjacentHTML('beforeend', renderProcessoCard(processo));
            });
        } else {
            processosContainer.innerHTML = '<p class="text-gray-600 text-center">Nenhum processo seletivo encontrado com os filtros aplicados.</p>';
        }
    }

    function limparFiltros() {
        filtroArea.value = "";
        filtroModalidade.value = "";
        filtroMes.value = "";
        aplicarFiltros(); // Re-apply filters to show all
    }

    // Event Listeners for filters and clear button
    filtroArea.addEventListener("change", aplicarFiltros);
    filtroModalidade.addEventListener("change", aplicarFiltros);
    filtroMes.addEventListener("change", aplicarFiltros);
    btnLimpar.addEventListener("click", limparFiltros);

    // Initial call to fetch and display processes when the page loads
    fetchAndDisplayProcessos();
});