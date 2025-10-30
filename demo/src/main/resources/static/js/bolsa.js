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
    const bolsasContainer = document.getElementById("bolsas-container"); // Where scholarship cards will be loaded
    const btnLimpar = document.getElementById("btn-limpar");

    let allBolsas = []; // To store all scholarships fetched from the API

    // Function to fetch and display scholarships
    async function fetchAndDisplayBolsas() {
        bolsasContainer.innerHTML = '<p class="text-gray-600 text-center col-span-2">Carregando oportunidades de bolsas...</p>'; // Loading feedback

        try {
            // Fetch from your backend API endpoint for scholarships
            const response = await fetch('http://localhost:8080/api/bolsas/todos'); 
            
            if (!response.ok) {
                if (response.status === 401) {
                    window.location.href = '/login'; // Redirect if not authenticated
                    return;
                }
                throw new Error(`Erro ao carregar bolsas: Status ${response.status}`);
            }

            const data = await response.json();

            if (data.sucesso && Array.isArray(data.bolsas)) {
                allBolsas = data.bolsas; // Store all fetched scholarships
                aplicarFiltros(); // Apply initial filters (show all)
            } else {
                console.error('Erro na resposta da API de bolsas:', data.erro || 'Formato de dados inválido.');
                bolsasContainer.innerHTML = '<p class="text-red-500 text-center col-span-2">Erro ao carregar oportunidades de bolsas.</p>';
            }

        } catch (error) {
            console.error('Erro na requisição da API de bolsas:', error);
            bolsasContainer.innerHTML = '<p class="text-red-500 text-center col-span-2">Não foi possível conectar ao servidor de bolsas.</p>';
        }
    }

    // Function to render a single scholarship card
    function renderBolsaCard(bolsa) {
        return `
            <div class="bolsa border rounded-xl p-4 shadow bg-white">
                <h3 class="text-xl font-semibold">${bolsa.titulo}</h3>
                <p class="text-gray-600">💰 ${bolsa.valor} | 📅 ${bolsa.duracaoMeses} | 👤 ${bolsa.publicoAlvo}</p>
                <p class="mt-2">${bolsa.descricao}</p>
                <ul class="mt-2 text-sm text-gray-700 list-disc list-inside">
                    <li>Requisitos: ${bolsa.requisitos || 'Não informado'}</li>
                    <li>Benefícios: ${bolsa.beneficios || 'Não informado'}</li>
                </ul>
                <a
                    href="${bolsa.linkInscricao}"
                    class="inline-block mt-4 bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700"
                >
                    Inscrever-se
                </a>
            </div>
        `;
    }

    // Function to apply filters and render scholarships
    function aplicarFiltros() {
        const area = filtroArea.value;
        const modalidade = filtroModalidade.value;
        const mes = filtroMes.value;

        bolsasContainer.innerHTML = ''; // Clear container before rendering

        const bolsasFiltradas = allBolsas.filter((bolsa) => {
            const matchArea = !area || (bolsa.area && bolsa.area.toLowerCase() === area.toLowerCase());
            const matchModalidade = !modalidade || (bolsa.modalidade && bolsa.modalidade.toLowerCase() === modalidade.toLowerCase());
            const matchMes = !mes || (bolsa.mesInicio && bolsa.mesInicio.toLowerCase() === mes.toLowerCase());
            return matchArea && matchModalidade && matchMes;
        });

        if (bolsasFiltradas.length > 0) {
            bolsasFiltradas.forEach((bolsa) => {
                bolsasContainer.insertAdjacentHTML('beforeend', renderBolsaCard(bolsa));
            });
        } else {
            bolsasContainer.innerHTML = '<p class="text-gray-600 text-center col-span-2">Nenhuma oportunidade de bolsa encontrada com os filtros aplicados.</p>';
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

    // Initial call to fetch and display scholarships when the page loads
    fetchAndDisplayBolsas();
});