// DOM elements
const taskForm = document.getElementById('task-form');
const taskTitleInput = document.getElementById('task-title');
const taskDescriptionInput = document.getElementById('task-description');
const tasksContainer = document.getElementById('tasks-container');

// Array to store tasks
let tasks = [];

// Function to add a new task
function addTask(title, description) {
    const task = {
        id: Date.now(), // Simple unique ID based on timestamp
        title: title,
        description: description,
        completed: false
    };
    tasks.push(task);
    renderTasks();
}

// Function to render all tasks
function renderTasks() {
    tasksContainer.innerHTML = ''; // Clear current tasks
    tasks.forEach(task => {
        const taskElement = document.createElement('div');
        taskElement.className = `task-item ${task.completed ? 'completed' : ''}`;
        taskElement.innerHTML = `
            <div class="task-content">
                <h3>${task.title}</h3>
                <p>${task.description || 'No description provided.'}</p>
            </div>
            <div class="task-actions">
                <button class="complete-btn" data-id="${task.id}">${task.completed ? 'Undo' : 'Complete'}</button>
                <button class="delete-btn" data-id="${task.id}">Delete</button>
            </div>
        `;
        tasksContainer.appendChild(taskElement);
    });
}

// Function to toggle task completion
function toggleTaskCompletion(id) {
    const task = tasks.find(t => t.id === id);
    if (task) {
        task.completed = !task.completed;
        renderTasks();
    }
}

// Function to delete a task
function deleteTask(id) {
    tasks = tasks.filter(t => t.id !== id);
    renderTasks();
}

// Event listener for form submission
taskForm.addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent page reload
    const title = taskTitleInput.value.trim();
    const description = taskDescriptionInput.value.trim();
    if (title) {
        addTask(title, description);
        taskTitleInput.value = ''; // Clear input fields
        taskDescriptionInput.value = '';
    } else {
        alert('Please enter a task title.');
    }
});

// Event delegation for task actions (complete and delete buttons)
tasksContainer.addEventListener('click', function(e) {
    if (e.target.classList.contains('complete-btn')) {
        const id = parseInt(e.target.getAttribute('data-id'));
        toggleTaskCompletion(id);
    } else if (e.target.classList.contains('delete-btn')) {
        const id = parseInt(e.target.getAttribute('data-id'));
        deleteTask(id);
    }
});

// Initialize with some sample tasks for demonstration
addTask('Complete project report', 'Finish the report by Friday.');
addTask('Buy groceries', 'Milk, eggs, bread, and fruits.');
addTask('Call dentist', 'Schedule an appointment for next week.');