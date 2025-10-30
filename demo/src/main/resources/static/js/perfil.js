AOS.init();

const editBtn = document.getElementById("edit-btn");
const saveBtn = document.getElementById("save-btn");
const nomeInput = document.getElementById("nome");
const profissaoInput = document.getElementById("profissao");
const cpfInput = document.getElementById("cpf"); // Hidden CPF input
const avatarUpload = document.getElementById("avatar-upload");
const uploadAvatarBtn = document.getElementById("upload-avatar-btn");
const avatarImg = document.getElementById("avatar-img");

// List containers
const formacaoAcademicaList = document.getElementById("formacaoAcademicaList");
const experienciaProfissionalList = document.getElementById("experienciaProfissionalList");
const areasInteresseList = document.getElementById("areasInteresseList");
const idiomasList = document.getElementById("idiomasList");
const participacoesList = document.getElementById("participacoesList");
const projetosDestacadosList = document.getElementById("projetosDestacadosList");

// Add buttons for dynamic lists
const addFormacaoBtn = document.getElementById("add-formacao-btn");
const addExperienciaBtn = document.getElementById("add-experiencia-btn");
const addAreaInteresseBtn = document.getElementById("add-area-interesse-btn");
const addIdiomaBtn = document.getElementById("add-idioma-btn");
const addParticipacaoBtn = document.getElementById("add-participacao-btn");
const addProjetoBtn = document.getElementById("add-projeto-btn");
const addSkillBtn = document.getElementById("add-skill-btn"); // New button for skills

const objetivosProfissionaisTextarea = document.getElementById("objetivosProfissionais");
const experienciaAcademicaTextarea = document.getElementById("experienciaAcademica");
const linkedinInput = document.getElementById("linkedin");
const githubInput = document.getElementById("github");
const portfolioInput = document.getElementById("portfolio");

const skillsEditSection = document.getElementById("skills-edit");
let skillsChart; // Chart.js instance

// --- Profile Data Loading and Saving ---

async function fetchProfileData() {
  try {
    const response = await fetch('http://localhost:8080/perfil/dados'); // Endpoint to get profile data
    if (!response.ok) {
      if (response.status === 401) {
        window.location.href = '/login'; // Redirect if not authenticated
        return;
      }
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();

    if (data.sucesso && data.perfil) {
      populateProfile(data.perfil);
    } else {
      console.error('Failed to load profile:', data.erro);
      // If profile not found for a new user, populate with defaults or empty values
      populateProfile({
        cpf: data.perfil ? data.perfil.cpf : '', // Use CPF from response if available, else empty
        nome: '', profissao: '', formacaoAcademica: [], experienciaProfissional: [],
        habilidades: {}, areasInteresse: [], objetivosProfissionais: '',
        experienciaAcademica: '', idiomas: [], participacoes: [],
        projetosDestacados: [], linkedin: '', github: '', portfolio: ''
      });
    }
  } catch (error) {
    console.error('Error fetching profile data:', error);
    mostrarNotificacao("Erro ao conectar com o servidor para carregar o perfil. ❌", 5000);
  }
}

function populateProfile(perfil) {
  nomeInput.value = perfil.nome || '';
  profissaoInput.value = perfil.profissao || '';
  cpfInput.value = perfil.cpf || ''; // Set hidden CPF

  // Populate dynamic lists (always initially as spans in view mode)
  populateDynamicList(formacaoAcademicaList, perfil.formacaoAcademica, 'li', 'span', '');
  populateDynamicList(experienciaProfissionalList, perfil.experienciaProfissional, 'li', 'span', '');
  populateDynamicList(areasInteresseList, perfil.areasInteresse, 'div', 'span', 'px-3 py-1 bg-gray-100 rounded-full');
  populateDynamicList(idiomasList, perfil.idiomas, 'li', 'span', 'bg-gray-100 p-2 rounded mb-3 block');
  populateDynamicList(participacoesList, perfil.participacoes, 'li', 'span', 'bg-gray-100 p-2 rounded mb-3 block');
  populateDynamicList(projetosDestacadosList, perfil.projetosDestacados, 'li', 'span', 'bg-gray-100 p-2 rounded');


  // Populate textareas
  objetivosProfissionaisTextarea.value = perfil.objetivosProfissionais || '';
  experienciaAcademicaTextarea.value = perfil.experienciaAcademica || '';

  // Populate links
  linkedinInput.value = perfil.linkedin || '';
  githubInput.value = perfil.github || '';
  portfolioInput.value = perfil.portfolio || '';

  // Populate skills chart and edit section
  // THIS IS THE FIX FOR "NOT GRABBING INFO"
  if (perfil.habilidades) {
    const labels = Object.keys(perfil.habilidades);
    const data = Object.values(perfil.habilidades);
    updateSkillsChart(labels, data);
    updateSkillsEditSection(perfil.habilidades); // This populates the edit fields
  } else {
      updateSkillsChart([], []); // Clear chart if no skills
      updateSkillsEditSection({}); // Clear edit section if no skills
  }


  // Handle avatar (if base64 string is stored)
  if (perfil.avatar) {
    avatarImg.src = perfil.avatar;
  }
}


/**
 * Generic function to populate dynamic lists with items.
 * @param {HTMLElement} container The parent element (e.g., ul, div)
 * @param {Array<string>} dataArray The array of strings to populate.
 * @param {string} wrapperTag 'li' or 'div' for the wrapper element.
 * @param {string} itemTag 'input' or 'span' for the actual content element.
 * @param {string} itemClassName CSS classes for the item element (span or input).
 */
function populateDynamicList(container, dataArray, wrapperTag, itemTag, itemClassName = '') {
    container.innerHTML = ''; // Clear existing content
    if (dataArray && Array.isArray(dataArray) && dataArray.length > 0) { // Check if dataArray is actually an array
        dataArray.forEach(item => {
            const wrapper = document.createElement(wrapperTag);
            const contentEl = document.createElement(itemTag);

            contentEl.textContent = item;
            contentEl.className = itemClassName;
            contentEl.setAttribute('data-editable-content', 'true'); // Mark as editable content

            wrapper.appendChild(contentEl);
            container.appendChild(wrapper);
        });
    }
}


// --- Edit Mode Toggling ---

function toggleEditMode(enable) {
  // Toggle readOnly for main input fields and textareas
  document.querySelectorAll('input:not(#avatar-upload):not([data-editable-list-input]), textarea').forEach(el => {
    el.readOnly = !enable;
    el.classList.toggle('bg-gray-100', !enable);
    el.classList.toggle('border-blue-500', enable);
    el.classList.toggle('border-transparent', !enable);
  });

  // Show/hide specific edit buttons
  uploadAvatarBtn.style.display = enable ? "block" : "none";
  saveBtn.style.display = enable ? "inline-block" : "none";
  editBtn.style.display = enable ? "none" : "inline-block";

  // Toggle visibility of add buttons for dynamic lists
  addFormacaoBtn.classList.toggle('hidden', !enable);
  addExperienciaBtn.classList.toggle('hidden', !enable);
  addAreaInteresseBtn.classList.toggle('hidden', !enable);
  addIdiomaBtn.classList.toggle('hidden', !enable);
  addParticipacaoBtn.classList.toggle('hidden', !enable);
  addProjetoBtn.classList.toggle('hidden', !enable);
  addSkillBtn.classList.toggle('hidden', !enable);

  // Toggle skills edit section visibility
  skillsEditSection.classList.toggle("hidden", !enable);

  // Convert spans to inputs or inputs back to spans for dynamic lists
  const dynamicLists = [
    formacaoAcademicaList, experienciaProfissionalList, areasInteresseList,
    idiomasList, participacoesList, projetosDestacadosList
  ];

  dynamicLists.forEach(list => {
    if (enable) {
      convertSpansToInputsInList(list);
    } else {
      convertInputsToSpansInList(list);
    }
  });

  // If exiting edit mode, ensure chart is updated from current inputs
  if (!enable) {
    updateChartFromInputs();
  }
}

/**
 * Converts `<span>` elements within a list container to editable `<input>` fields.
 * Includes a remove button for each item.
 * @param {HTMLElement} container The list container (e.g., ul, div).
 */
function convertSpansToInputsInList(container) {
    // Select the direct children (e.g., li or div) that contain the content span
    container.querySelectorAll('[data-editable-content="true"]').forEach(contentEl => {
        const wrapper = contentEl.parentElement; // This is the li or div wrapper

        const input = document.createElement('input');
        input.type = 'text';
        input.value = contentEl.textContent.trim();
        input.className = 'w-full p-1 border border-blue-500 rounded mb-1';
        input.setAttribute('data-editable-list-input', 'true'); // Mark as editable list input

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'X';
        deleteBtn.className = 'ml-2 text-red-500 hover:text-red-700 text-sm';
        deleteBtn.type = 'button'; // Prevent form submission
        deleteBtn.onclick = (e) => {
            e.preventDefault();
            wrapper.remove(); // Remove the entire wrapper (li or div)
        };

        // Clear wrapper and append new elements
        wrapper.innerHTML = '';
        wrapper.appendChild(input);
        wrapper.appendChild(deleteBtn);
    });
}

/**
 * Converts editable `<input>` fields within a list container back to `<span>` elements.
 * Removes empty items.
 * @param {HTMLElement} container The list container (e.g., ul, div).
 */
function convertInputsToSpansInList(container) {
    // Select the direct children (e.g., li or div) that contain the input
    const itemsToRemove = [];
    container.querySelectorAll('[data-editable-list-input="true"]').forEach(input => {
        const wrapper = input.parentElement; // This is the li or div wrapper

        if (!input.value.trim()) {
            itemsToRemove.push(wrapper); // Mark for removal if empty
            return;
        }

        const span = document.createElement('span');
        span.textContent = input.value.trim();
        // Re-apply original class names (if applicable, depends on how your CSS works)
        if (container.id === 'areasInteresseList') {
            span.className = 'px-3 py-1 bg-gray-100 rounded-full';
        } else if (container.id === 'idiomasList' || container.id === 'participacoesList' || container.id === 'projetosDestacadosList') {
            span.className = 'bg-gray-100 p-2 rounded mb-3 block';
        } else {
             span.className = 'w-full bg-transparent'; // Default for simple lists
        }
        span.setAttribute('data-editable-content', 'true'); // Mark again for future editing

        wrapper.innerHTML = ''; // Clear wrapper
        wrapper.appendChild(span);
    });

    itemsToRemove.forEach(item => item.remove());
}


/**
 * Adds a new editable item to a dynamic list.
 * @param {HTMLElement} listContainer The parent element (e.g., ul, div)
 * @param {string} itemClassName CSS classes to apply to the input.
 */
function addListItemFromPrompt(listContainer, itemClassName = '') {
    const itemText = prompt("Adicionar novo item:");
    if (itemText) {
        // Determine the wrapper tag based on the list container's tag
        const wrapperTag = (listContainer.tagName === 'UL' || listContainer.tagName === 'OL') ? 'li' : 'div';
        const wrapper = document.createElement(wrapperTag);

        const contentEl = document.createElement('input'); // Always create an input when adding via prompt
        contentEl.type = 'text';
        contentEl.value = itemText;
        contentEl.readOnly = false;
        contentEl.className = 'w-full p-1 border border-blue-500 rounded mb-1'; // Editable style
        if (itemClassName) {
            contentEl.className += ' ' + itemClassName; // Add specific classes if provided
        }
        contentEl.setAttribute('data-editable-list-input', 'true'); // Mark as editable list input

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'X';
        deleteBtn.className = 'ml-2 text-red-500 hover:text-red-700 text-sm';
        deleteBtn.type = 'button'; // Prevent form submission
        deleteBtn.onclick = (e) => { e.preventDefault(); wrapper.remove(); };

        wrapper.appendChild(contentEl);
        wrapper.appendChild(deleteBtn);
        listContainer.appendChild(wrapper);

        contentEl.focus(); // Focus on the newly added input
    }
}


// --- Event Listeners ---

editBtn.addEventListener("click", () => toggleEditMode(true));

saveBtn.addEventListener("click", async () => {
  const profileData = collectProfileData(); // Collect data while inputs are still active
  toggleEditMode(false); // Switch to view mode

  try {
    const response = await fetch('http://localhost:8080/perfil/salvar', { // Ensure absolute URL for fetch
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(profileData),
    });

    const result = await response.json();

    if (result.sucesso) {
      mostrarNotificacao("Perfil salvo com sucesso! ✅");
      // Re-fetch and populate profile to ensure consistent display after save
      // This is crucial as the backend might return normalized data
      fetchProfileData();
    } else {
      mostrarNotificacao(`Erro ao salvar perfil: ${result.mensagem} ❌`, 5000);
      // Revert to edit mode or show specific errors if needed
      toggleEditMode(true); // Allow user to fix errors
    }
  } catch (error) {
    console.error('Erro ao salvar perfil:', error);
    mostrarNotificacao("Erro ao conectar com o servidor para salvar o perfil. ❌", 5000);
    toggleEditMode(true); // Allow user to try again
  }
});

// Helper to collect all profile data from the form
function collectProfileData() {
  const data = {
    cpf: cpfInput.value, // Crucial for backend to identify user
    nome: nomeInput.value,
    profissao: profissaoInput.value,
    formacaoAcademica: collectListItems(formacaoAcademicaList),
    experienciaProfissional: collectListItems(experienciaProfissionalList),
    habilidades: collectSkills(),
    areasInteresse: collectListItems(areasInteresseList),
    objetivosProfissionais: objetivosProfissionaisTextarea.value,
    experienciaAcademica: experienciaAcademicaTextarea.value,
    idiomas: collectListItems(idiomasList),
    participacoes: collectListItems(participacoesList),
    projetosDestacados: collectListItems(projetosDestacadosList),
    linkedin: linkedinInput.value,
    github: githubInput.value,
    portfolio: portfolioInput.value,
    // avatar: avatarImg.src // If you want to save base64 avatar to JSON
  };
  return data;
}

// Helper to collect data from a dynamic list (inputs or spans)
// This function needs to specifically target the editable inputs
function collectListItems(container) {
    const items = [];
    // Collect from inputs first (if in edit mode)
    container.querySelectorAll('[data-editable-list-input="true"]').forEach(inputEl => {
        if (inputEl.value.trim()) {
            items.push(inputEl.value.trim());
        }
    });
    // If no inputs (i.e., in view mode or just loaded), collect from spans
    if (items.length === 0) { // Only do this if no inputs were found
        container.querySelectorAll('[data-editable-content="true"]').forEach(spanEl => {
             if (spanEl.textContent.trim()) {
                 items.push(spanEl.textContent.trim());
             }
        });
    }
    return items;
}

// Helper to collect skills from the edit section
function collectSkills() {
  const skills = {};
  skillsEditSection.querySelectorAll('.flex.space-x-2').forEach(skillRow => {
    const nameInput = skillRow.querySelector('.skill-name');
    const valueInput = skillRow.querySelector('.skill-value');
    if (nameInput && valueInput && nameInput.value.trim()) {
      skills[nameInput.value.trim()] = Number(valueInput.value) || 0;
    }
  });
  return skills;
}


// --- Image Upload ---
uploadAvatarBtn.addEventListener("click", () => avatarUpload.click());
avatarUpload.addEventListener("change", (e) => {
  const file = e.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = function (e) {
      avatarImg.src = e.target.result;
    };
    reader.readAsDataURL(file);
  }
});

// --- Chart.js Initialization and Update ---
const ctx = document.getElementById("skillsChart").getContext("2d");
function initializeSkillsChart() {
    skillsChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels: [], // Will be populated dynamically
            datasets: [{
                label: "Nível de Proficiência (%)",
                data: [], // Will be populated dynamically
                backgroundColor: [
                    "rgba(59, 130, 246, 0.7)", "rgba(234, 179, 8, 0.7)", "rgba(79, 70, 229, 0.7)",
                    "rgba(220, 38, 38, 0.7)", "rgba(16, 185, 129, 0.7)", "rgba(139, 92, 246, 0.7)",
                    "rgba(0, 150, 136, 0.7)", "rgba(255, 87, 34, 0.7)", "rgba(121, 85, 72, 0.7)" // Added more colors
                ],
                borderRadius: 5,
            }, ],
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true, max: 100, ticks: { stepSize: 10 } },
            },
            plugins: { legend: { display: false } },
        },
    });
}

function populateProfile(perfil) {
  nomeInput.value = perfil.nome || ''; // Populates 'nome' input
  profissaoInput.value = perfil.profissao || ''; // Populates 'profissao' input
  cpfInput.value = perfil.cpf || ''; // Populates hidden 'cpf' input

  // Populate dynamic lists (always initially as spans in view mode)
  // These functions iterate through the arrays in perfil and create span elements
  populateDynamicList(formacaoAcademicaList, perfil.formacaoAcademica, 'li', 'span', '');
  populateDynamicList(experienciaProfissionalList, perfil.experienciaProfissional, 'li', 'span', '');
  populateDynamicList(areasInteresseList, perfil.areasInteresse, 'div', 'span', 'px-3 py-1 bg-gray-100 rounded-full');
  populateDynamicList(idiomasList, perfil.idiomas, 'li', 'span', 'bg-gray-100 p-2 rounded mb-3 block');
  populateDynamicList(participacoesList, perfil.participacoes, 'li', 'span', 'bg-gray-100 p-2 rounded mb-3 block');
  populateDynamicList(projetosDestacadosList, perfil.projetosDestacados, 'li', 'span', 'bg-gray-100 p-2 rounded');


  // Populate textareas
  objetivosProfissionaisTextarea.value = perfil.objetivosProfissionais || '';
  experienciaAcademicaTextarea.value = perfil.experienciaAcademica || '';

  // Populate links
  linkedinInput.value = perfil.linkedin || '';
  githubInput.value = perfil.github || '';
  portfolioInput.value = perfil.portfolio || '';

  // Populate skills chart and edit section
  // THIS IS WHERE 'habilidades' are handled
  if (perfil.habilidades && Object.keys(perfil.habilidades).length > 0) { // Check if 'habilidades' exist and are not empty
    const labels = Object.keys(perfil.habilidades);
    const data = Object.values(perfil.habilidades);
    updateSkillsChart(labels, data); // Updates the Chart.js graph
    updateSkillsEditSection(perfil.habilidades); // Populates the hidden edit section inputs
  } else {
      updateSkillsChart([], []); // Clear chart if no skills
      updateSkillsEditSection({}); // Clear edit section if no skills
  }

  // Handle avatar (if base64 string is stored)
  if (perfil.avatar) {
    avatarImg.src = perfil.avatar;
  }
}

initializeSkillsChart(); // Initialize the chart with empty data initially

function updateSkillsChart(labels, data) {
    if (skillsChart) {
        skillsChart.data.labels = labels;
        skillsChart.data.datasets[0].data = data;
        skillsChart.update();
    }
}

// Function to update skills edit section with inputs
function updateSkillsEditSection(skills) {
    skillsEditSection.innerHTML = ''; // Clear existing inputs
    for (const [name, value] of Object.entries(skills)) {
        addSkillInput(name, value);
    }
}

// Function to add a single skill input row
function addSkillInput(name = '', value = 0) {
    const skillRow = document.createElement('div');
    skillRow.className = 'flex space-x-2';
    skillRow.innerHTML = `
        <input type="text" class="skill-name border p-1 rounded w-1/2" value="${name}" />
        <input type="number" class="skill-value border p-1 rounded w-1/2" value="${value}" min="0" max="100" />
        <button class="remove-skill-btn text-red-500 hover:text-red-700 text-sm" type="button">X</button>
    `;
    skillsEditSection.appendChild(skillRow);

    skillRow.querySelector('.remove-skill-btn').onclick = (e) => {
        e.preventDefault();
        skillRow.remove();
        updateChartFromInputs(); // Update chart after removing skill
    };
    // Update chart instantly when skill value changes
    skillRow.querySelector('.skill-name').addEventListener('input', updateChartFromInputs);
    skillRow.querySelector('.skill-value').addEventListener('input', updateChartFromInputs);
}

// Attach event listener to add skill button
addSkillBtn.addEventListener('click', (e) => {
    e.preventDefault();
    addSkillInput(); // Add a new empty skill row
});


// Call this function when exiting edit mode to update chart from current inputs
function updateChartFromInputs() {
  const labels = [];
  const values = [];
  skillsEditSection.querySelectorAll('.flex.space-x-2').forEach(skillRow => {
    const nameInput = skillRow.querySelector('.skill-name');
    const valueInput = skillRow.querySelector('.skill-value');
    if (nameInput && nameInput.value.trim() && valueInput) {
      labels.push(nameInput.value.trim());
      values.push(Number(valueInput.value) || 0);
    }
  });
  updateSkillsChart(labels, values);
}


// --- Agenda & Chatbot (Existing Functions - no changes needed, just keeping them for context) ---
const eventos = {
  "2025-01-15": [
    {
      titulo: "Início do Semestre",
      descricao:
        "Aulas do primeiro semestre começam às 08h00 em todos os campi da UEG. Apresentação dos professores e entrega de cronogramas. Término às 17h00.",
    },
  ],
  "2025-01-25": [
    {
      titulo: "Integração dos Calouros",
      descricao:
        "Atividades de recepção aos novos alunos das 17h00 às 19h00 com música ao vivo, coffee break e visita guiada pelo campus.",
    },
  ],
  "2025-02-20": [
    {
      titulo: "Workshop de Tecnologia",
      descricao:
        "Evento com especialistas em TI das 14h00 às 18h00 no auditório central. Serão abordados temas como cibersegurança, inteligência artificial e mercado de trabalho.",
    },
  ],
  "2025-02-28": [
    {
      titulo: "Oficina de Currículo e LinkedIn",
      descricao:
        "A oficina será das 10h00 às 12h00 na sala 204, bloco B. Ajuda prática para montar currículo e perfil profissional.",
    },
  ],
  "2025-03-10": [
    {
      titulo: "Palestra de Carreira",
      descricao:
        "Das 09h30 às 11h00, no auditório 2, com a presença de ex-alunos bem-sucedidos que compartilharão suas trajetórias profissionais.",
    },
  ],
  "2025-03-22": [
    {
      titulo: "Rodada de Mentorias",
      descricao:
        "Mentores convidados oferecerão orientações individuais sobre carreira e projetos acadêmicos, das 13h00 às 16h00, sala 305.",
    },
  ],
  "2025-04-05": [
    {
      titulo: "Abertura da Semana Cultural",
      descricao:
        "Cerimônia de abertura às 08h00 com apresentações musicais e artísticas. A semana segue até 11/04 com programação diversa.",
    },
  ],
  "2025-04-09": [
    {
      titulo: "Exposição de Arte Estudantil",
      descricao:
        "Mostra permanente na biblioteca, das 09h00 às 19h00, com trabalhos de alunos da área de artes visuais.",
    },
  ],
  "2025-05-18": [
    {
      titulo: "Feira de Estágios",
      descricao:
        "Evento das 13h00 às 17h00 com empresas oferecendo estágios nas áreas de tecnologia, educação e saúde. Levar currículo impresso.",
    },
  ],
  "2025-05-22": [
    {
      titulo: "Palestra: Mercado de Trabalho em 2025",
      descricao:
        "Com especialistas em RH, às 10h00 no auditório 1. Discussão sobre as tendências e demandas do mercado.",
    },
  ],
  "2025-06-12": [
    {
      titulo: "Provas Finais de Estágio",
      descricao:
        "Aplicação das provas finais dos estágios curriculares obrigatórios. Início às 08h00 e término previsto para 12h00, conforme turma.",
    },
  ],
  "2025-06-20": [
    {
      titulo: "Revisão de Relatórios de Estágio",
      descricao:
        "Encontro com orientadores às 14h00 na sala 112 para revisão dos relatórios finais antes da entrega.",
    },
  ],
  "2025-07-04": [
    {
      titulo: "Início das Inscrições para Estágio em TI",
      descricao:
        "Abertura às 00h00 do formulário online. As inscrições vão até 20/07. Estágio para alunos do 5º ao 8º período de cursos de tecnologia.",
    },
  ],
  "2025-07-15": [
    {
      titulo: "Capacitação em Soft Skills",
      descricao:
        "Minicurso online gratuito com duração de 3 dias, começando em 15/07. Inscrição via site da UEG.",
    },
  ],
  "2025-08-10": [
    {
      titulo: "Feira Científica da UEG",
      descricao:
        "Das 08h00 às 18h00 no pátio central. Estudantes apresentarão projetos e experimentos nas áreas de ciência e tecnologia.",
    },
  ],
  "2025-08-14": [
    {
      titulo: "Processos Seletivos do SENAC",
      descricao:
        "Início das inscrições para cursos técnicos presenciais e EAD do SENAC. Processo seletivo vai até 31/08. Inscreva-se pelo site oficial.",
    },
  ],
  "2025-08-23": [
    {
      titulo: "Encerramento das Inscrições - Country 2025",
      descricao:
        "Prazo final para se inscrever no programa de intercâmbio cultural Country 2025. Inscrições encerram às 23h59.",
    },
  ],
  "2025-09-30": [
    {
      titulo: "Premiação do Concurso Literário",
      descricao:
        "Cerimônia às 19h00 no Teatro da UEG. Os três melhores textos serão lidos e premiados com bolsas de estudo e kits de livros.",
    },
  ],
  "2025-09-12": [
    {
      titulo: "Bate-papo com Autores",
      descricao:
        "Roda de conversa com autores goianos na biblioteca central às 10h00. Aberto ao público.",
    },
  ],
  "2025-10-31": [
    {
      titulo: "Tecnologia na UEG - Halloween Edição",
      descricao:
        "Das 18h00 às 22h00, evento temático com exposição de projetos de tecnologia. Concurso de fantasias tecnológicas com premiações.",
    },
  ],
  "2025-10-20": [
    {
      titulo: "Maratona de Programação",
      descricao:
        "Competição de programação em duplas, das 14h00 às 17h00, na sala de informática 01. Premiação no final do evento.",
    },
  ],
  "2025-11-20": [
    {
      titulo: "Mostra de Projetos Interdisciplinares",
      descricao:
        "Apresentações de TCCs e trabalhos integradores das 08h00 às 17h00. Banca avaliadora presente. Aberto ao público.",
    },
  ],
  "2025-11-27": [
    {
      titulo: "Simpósio de Pesquisa Acadêmica",
      descricao:
        "Das 09h00 às 18h00. Palestras, apresentações de artigos e mesas-redondas. Submissões até 10/11.",
    },
  ],
  "2025-12-15": [
    {
      titulo: "Processos Seletivos da UEFG",
      descricao:
        "Início das inscrições para cursos de graduação. Processo seletivo via análise de histórico escolar. Vai até 10/01/2026.",
    },
  ],
  "2025-12-18": [
    {
      titulo: "Live de Orientações Acadêmicas",
      descricao:
        "Transmissão às 19h00 com dicas sobre documentação e entrevistas do processo seletivo UEFG. Link será enviado por e-mail aos inscritos.",
    },
  ],
};

// --- Agenda Initialization after DOM loaded ---
window.addEventListener("DOMContentLoaded", () => {
    // Call fetchProfileData to load user profile from backend
    fetchProfileData();

    const tituloMes = document.getElementById("titulo-mes");
    const gradeDias = document.getElementById("grade-dias");
    const dataSelecionadaEl = document.getElementById("data-selecionada");
    const notaDia = document.getElementById("nota-dia");
    const urgentTextarea = document.getElementById("urgent-textarea");
    const notesTextarea = document.getElementById("notes-textarea");
    const listaTodo = document.getElementById("lista-todo");
    const btnAddTarefa = document.getElementById("btn-add-tarefa");
    const salvarNotaBtn = document.getElementById("salvar-nota");
    const statusNota = document.getElementById("status-nota");
    const containerAgenda = document.getElementById("container-agenda");
    const btnFecharAgenda = document.getElementById("btn-fechar-agenda");
    const eventosDoDiaDiv = document.getElementById("eventos-do-dia");
    const btnApagarNota = document.getElementById("btn-apagar-nota");
    const detalheEl = document.getElementById("evento-detalhado");
    const detalheConteudo = document.getElementById("evento-detalhado-conteudo");

    // Close agenda on X button click
    btnFecharAgenda.addEventListener("click", (e) => {
        e.stopPropagation();
        containerAgenda.classList.add("agenda-fechada");
    });

    let hoje = new Date();
    let mesAtual = hoje.getMonth();
    let anoAtual = hoje.getFullYear();
    let diaSelecionado = null;
    let tarefas = [];

    function formatDateKey(date) {
        return date.toISOString().split("T")[0];
    }

    function renderTituloMes() {
        const meses = [
            "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
            "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro",
        ];
        tituloMes.textContent = `${meses[mesAtual]} ${anoAtual}`;
    }

    function hasSavedData(dateKey) {
        return localStorage.getItem(dateKey) !== null;
    }

    function gerarDias() {
        gradeDias.innerHTML = "";
        const primeiroDia = new Date(anoAtual, mesAtual, 1).getDay();
        const diasNoMes = new Date(anoAtual, mesAtual + 1, 0).getDate();

        for (let i = 0; i < primeiroDia; i++) {
            const vazio = document.createElement("div");
            vazio.classList.add("dia");
            gradeDias.appendChild(vazio);
        }

        for (let i = 1; i <= diasNoMes; i++) {
            const dia = document.createElement("div");
            dia.textContent = i;
            dia.classList.add("dia");
            dia.style.cursor = "pointer";

            if (diaSelecionado === i) dia.classList.add("destaque");

            const dataKey = `${anoAtual}-${String(mesAtual + 1).padStart(2, "0")}-${String(i).padStart(2, "0")}`;

            if (eventos[dataKey]) {
                dia.classList.add("dia-evento");
                dia.title = eventos[dataKey].map((ev) => ev.titulo).join(", ");
            }

            if (hasSavedData(dataKey)) dia.classList.add("dia-salvo");

            dia.addEventListener("click", () => {
                diaSelecionado = i;
                carregarDadosDoDia();
                gerarDias();
                containerAgenda.classList.remove("agenda-fechada");
            });

            gradeDias.appendChild(dia);
        }
    }

    function renderListaTarefas() {
        listaTodo.innerHTML = "";
        tarefas.forEach((tarefa, index) => {
            const li = document.createElement("li");
            li.classList.add("lista-check-li");
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            const span = document.createElement("span");
            span.textContent = tarefa;
            span.addEventListener("click", () => {
                tarefas.splice(index, 1);
                renderListaTarefas();
            });
            li.appendChild(checkbox);
            li.appendChild(span);
            listaTodo.appendChild(li);
        });
    }

    function carregarDadosDoDia() {
        if (!diaSelecionado) return;

        const key = formatDateKey(new Date(anoAtual, mesAtual, diaSelecionado));
        dataSelecionadaEl.textContent = `Anotações para: ${key}`;

        // Exibir descrições no balão lateral
        const eventosDoDia = eventos[key] || [];
        if (eventosDoDia.length > 0) {
            detalheConteudo.innerHTML = "";
            eventosDoDia.forEach((evento) => {
                const descricao = document.createElement("div");
                descricao.textContent = evento.descricao;
                descricao.classList.add("evento-expandido-desc");
                detalheConteudo.appendChild(descricao);
            });
            detalheEl.classList.remove("hidden");

            // ⏱️ Auto-close after 15 seconds
            setTimeout(() => {
                detalheEl.classList.add("hidden");
            }, 15000);
        } else {
            detalheEl.classList.add("hidden");
        }

        const saved = localStorage.getItem(key);
        if (saved) {
            const data = JSON.parse(saved);
            notaDia.value = data.nota || "";
            urgentTextarea.value = data.urgent || "";
            notesTextarea.value = data.notes || "";
            tarefas = data.tarefas || [];
        } else {
            notaDia.value = urgentTextarea.value = notesTextarea.value = "";
            tarefas = [];
        }

        renderListaTarefas();
    }

    btnAddTarefa.addEventListener("click", () => {
        const nova = prompt("Digite a nova tarefa:");
        if (nova) {
            tarefas.push(nova);
            renderListaTarefas();
        }
    });

    salvarNotaBtn.addEventListener("click", () => {
        if (!diaSelecionado) return showAlert("Selecione um dia primeiro."); // Use custom alert
        const key = formatDateKey(new Date(anoAtual, mesAtual, diaSelecionado));
        localStorage.setItem(
            key,
            JSON.stringify({
                nota: notaDia.value,
                urgent: urgentTextarea.value,
                notes: notesTextarea.value,
                tarefas: tarefas,
            })
        );
        mostrarNotificacao("Nota salva com sucesso! ✅"); // Use custom notification
        gerarDias();
    });

    btnApagarNota.addEventListener("click", () => {
        if (!diaSelecionado) return showAlert("Selecione um dia primeiro."); // Use custom alert
        const key = formatDateKey(new Date(anoAtual, mesAtual, diaSelecionado));
        if (confirm("Tem certeza que deseja apagar tudo?")) {
            localStorage.removeItem(key);
            notaDia.value = urgentTextarea.value = notesTextarea.value = "";
            tarefas = [];
            renderListaTarefas();
            mostrarNotificacao("Notas apagadas! ❌"); // Use custom notification
            gerarDias();
        }
    });

    document.getElementById("btn-anterior").addEventListener("click", () => {
        mesAtual--;
        if (mesAtual < 0) {
            mesAtual = 11;
            anoAtual--;
        }
        diaSelecionado = null;
        renderTituloMes();
        gerarDias();
    });

    document.getElementById("btn-proximo").addEventListener("click", () => {
        mesAtual++;
        if (mesAtual > 11) {
            mesAtual = 0;
            anoAtual++;
        }
        diaSelecionado = null;
        renderTituloMes();
        gerarDias();
    });

    renderTituloMes();
    gerarDias();
});


// --- Motivational Phrase & Chatbot (Existing Functions) ---
const phrases = [
  "Oi sou o mascote da BTA, me chamo Geni de Genius para representar nós dois",
  "Que bom te ver denovo, já é de casa né",
  "Fique de olho nas novidades!",
  "Já viu os novos eventos por ai? tudo nota 10!",
  "Dando uma olhada por ai né, só tem qualidade massa aqui!",
  "Cada vez mais perto do sucesso!",
  "Nunca desista de ir mais longe!",
  "Você está indo muito bem!",
  "Continue se esforçando!",
  "Cada passo conta!",
  "Seu talento é único!",
  "Acredite no seu potencial!",
];
const phraseEl = document.getElementById("motivational-phrase");
let phraseIndex = 0;
function showPhrase() {
  phraseEl.textContent = phrases[phraseIndex];
  phraseEl.classList.add("show");
  setTimeout(() => {
    phraseEl.classList.remove("show");
    phraseIndex = (phraseIndex + 1) % phrases.length;
    setTimeout(showPhrase, 1000);
  }, 9000);
}
showPhrase();

document.getElementById("chat-botao").addEventListener("click", () => {
  const chat = document.getElementById("chat-box");
  chat.style.display = chat.style.display === "block" ? "none" : "block";
});
const chatBox = document.getElementById("chat-box");
const chatMensagens = document.getElementById("chat-mensagens");
const chatInput = document.getElementById("chat-input");
const chatTarget = document.getElementById("chat-target");
chatTarget.addEventListener("change", () => {
  chatMensagens.innerHTML = "";
});

const respostasMascote = [
  "Olá!Estou aqui para ajudar!",
  "Você já conferiu suas habilidades?",
  "Vamos encontrar a melhor vaga para você.",
  "Se não conseguimos resolver entre diretamente em contato com a equipe de suporte (...)!",
];

chatInput.addEventListener("keypress", (e) => {
  if (e.key === "Enter") {
    const msg = chatInput.value.trim();
    if (!msg) return;
    const destino = chatTarget.value;

    chatMensagens.innerHTML += `<div><strong>Você:</strong> ${msg}</div>`;

    if (destino === "mascote") {
      const resposta =
        respostasMascote[Math.floor(Math.random() * respostasMascote.length)];
      setTimeout(() => {
        chatMensagens.innerHTML += `<div><strong>Mascote:</strong> ${resposta}</div>`;
        chatMensagens.scrollTop = chatMensagens.scrollHeight;
      }, 1000);
    } else {
      setTimeout(() => {
        chatMensagens.innerHTML += `<div><strong>${destino}:</strong> (resposta simulada)</div>`;
        chatMensagens.scrollTop = chatMensagens.scrollHeight;
      }, 1000);
    }

    chatInput.value = "";
    chatMensagens.scrollTop = chatMensagens.scrollHeight;
  }
});

// Custom Notification Function (from login.js)
function mostrarNotificacao(mensagem, duracao = 4000) {
  const container = document.getElementById("notificacoes-container");
  const notif = document.createElement("div");
  notif.className = "notificacao";
  notif.textContent = mensagem;
  container.appendChild(notif);

  setTimeout(() => notif.classList.add("show"), 50);

  setTimeout(() => {
    notif.classList.remove("show");
    setTimeout(() => notif.remove(), 500);
  }, duracao);
}

window.addEventListener("load", () => {
  const vagas = document.querySelectorAll("#vagas-lista input");
  vagas.forEach((vaga, index) => {
    setTimeout(() => {
      mostrarNotificacao(vaga.value);
    }, index * 1500);
  });
});

function toggleTheme() {
  const html = document.documentElement;
  const isDark = html.getAttribute("data-theme") === "dark";
  html.setAttribute("data-theme", isDark ? "light" : "dark");
}

// Helper to add a dynamic list item from prompt
function addListItemFromPrompt(listContainer, itemClassName = '') {
    const itemText = prompt("Adicionar novo item:");
    if (itemText) {
        const wrapperTag = (listContainer.tagName === 'UL' || listContainer.tagName === 'OL') ? 'li' : 'div';
        const wrapper = document.createElement(wrapperTag);

        const contentEl = document.createElement('input'); // Always create an input when adding via prompt
        contentEl.type = 'text';
        contentEl.value = itemText;
        contentEl.readOnly = false;
        contentEl.className = 'w-full p-1 border border-blue-500 rounded mb-1'; // Editable style
        if (itemClassName) {
            contentEl.className += ' ' + itemClassName;
        }
        contentEl.setAttribute('data-editable-list-input', 'true'); // Mark as editable list input

        const deleteBtn = document.createElement('button');
        deleteBtn.textContent = 'X';
        deleteBtn.className = 'ml-2 text-red-500 hover:text-red-700 text-sm';
        deleteBtn.type = 'button'; // Prevent form submission
        deleteBtn.onclick = (e) => { e.preventDefault(); wrapper.remove(); };

        wrapper.appendChild(contentEl);
        wrapper.appendChild(deleteBtn);
        listContainer.appendChild(wrapper);

        contentEl.focus(); // Focus on the newly added input
    }
}

// Add event listeners for dynamic add buttons
addFormacaoBtn.addEventListener('click', (e) => { e.preventDefault(); addListItemFromPrompt(formacaoAcademicaList, 'w-full border-b border-transparent focus:border-blue-500'); });
addExperienciaBtn.addEventListener('click', (e) => { e.preventDefault(); addListItemFromPrompt(experienciaProfissionalList, 'w-full border-b border-transparent focus:border-blue-500'); });
addAreaInteresseBtn.addEventListener('click', (e) => { e.preventDefault(); addListItemFromPrompt(areasInteresseList, 'px-3 py-1 bg-gray-100 rounded-full'); });
addIdiomaBtn.addEventListener('click', (e) => { e.preventDefault(); addListItemFromPrompt(idiomasList, 'bg-gray-100 p-2 rounded mb-3 block'); });
addParticipacaoBtn.addEventListener('click', (e) => { e.preventDefault(); addListItemFromPrompt(participacoesList, 'bg-gray-100 p-2 rounded mb-3 block'); });
addProjetoBtn.addEventListener('click', (e) => { e.preventDefault(); addListItemFromPrompt(projetosDestacadosList, 'bg-gray-100 p-2 rounded'); });

// Initial call to fetch profile data when the page loads
document.addEventListener("DOMContentLoaded", fetchProfileData);