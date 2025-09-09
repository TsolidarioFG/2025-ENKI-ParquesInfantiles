# 🌍 Web application for analyzing and improving accesibility in playgrounds for children with disabilities

This project is a **Final Degree Project** developed in collaboration with **Fundación ENKI**.  
Its goal is to provide an **accessible web platform** that modernizes and expands ENKI’s printed guide on playground accessibility in A Coruña.  

The platform offers an **interactive map**, accessibility filters, and user contributions to ensure families, educators, therapists, and professionals have **reliable and up-to-date information** about inclusive playgrounds.  

---

## ✨ Motivation

Accessibility is a **fundamental right** that ensures equal participation in society for people with disabilities. While improvements have been made in transportation, housing, and public buildings, **playgrounds are often overlooked**.  

Playgrounds are more than recreational areas — they are **environments for learning, socializing, and autonomy**, particularly important for children with disabilities and their families. However, many still lack inclusive designs, making them difficult to use.  

Fundación ENKI previously created a **printed guide** of accessible playgrounds in A Coruña, describing features such as:
- Wheelchair accessibility  
- Sensory-stimulating elements  
- Nearby amenities (restrooms, fountains, etc.)  

Although valuable, a printed guide has limitations:
- Hard to update  
- Physical copies are not always accessible  
- Not practical to carry everywhere  

This project addresses that gap with a **modern web application** that transforms the printed guide into a **dynamic, collaborative platform**.

---

## 🎯 Objectives

The project pursues the following specific objectives:

- 🌐 Develop an **accessible web application** with an interactive map and comprehensive accessibility filters.  
- 🏞️ Provide **detailed playground data**, including physical and sensory access, stimuli, and nearby services.  
- 👥 Enable **registered users** to suggest new parks, update information, and submit reviews.  
- ✅ Implement **administrator moderation** to ensure data quality and reliability.  
- 📱 Ensure **responsive and accessible design**, following web accessibility guidelines.  
- 💻 Maintain an **open-source, maintainable codebase** with best practices.  
- 🤝 Collaborate with **Fundación ENKI**, integrating their existing data and expertise.  

---

## ⚙️ Tech Stack

- **Frontend:** [Vue.js]  
- **Backend:** [Spring Boot]  
- **Database:** [PostgreSQL]  
- **Authentication:** Role-based access control  
- **Deployment:** Open-source, maintainable architecture  

---

## 🚀 Running the Project

### 1. Clone the repository
```bash
git clone https://github.com/TsolidarioFG/2025-ENKI-ParquesInfantiles.git
cd 2025-ENKI-ParquesInfantiles
```

### 2. Run the frontend (Vue.js)

Inside the client folder:
```bash
cd client
npm install
npm run
```

By default, it runs on http://localhost:5173 (or the port defined in Vue config).

### 3. Run the backend (Spring Boot)

Inside the server folder:
```bash
cd server
./mvnw spring-boot:run
```

The backend will start on http://localhost:8080.
