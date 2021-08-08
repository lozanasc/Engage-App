import React , { useState, useContext, useEffect } from 'react';
import toast, { Toaster } from 'react-hot-toast';
import './dashboard.content.css';
import { AuthContext } from '../../../context/admin.context';

const notifyErr = () => toast.error('Something went wrong, no changes made!', {
    style: {
        fontFamily: "Montserrat"
    }
});

const notifySucc = (msg) => toast.success(msg, {
    style: {
        fontFamily: "Montserrat, sans-serif"
    }
});

const Projects = () => {
    
    const {admin_name, get_projects, projects} = useContext(AuthContext);
    
    const [view_project, set_view_project] = useState(false);
    const [edit_project, set_edit_project] = useState(false);
    const [new_project, set_new_project] = useState(false);
    
    // New project hooks
    const [project_focus, set_project_focus] = useState(false);
    const [project_name, set_project_name] = useState('');
    const [project_description, set_project_description] = useState('');
    const [project_image, set_project_image] = useState({});

    // const [id_editable, set_id_editable] = useState(null);
    const [new_name, set_new_name] = useState('');
    const [new_description, set_new_description] = useState('');
    const [new_image, set_new_image] = useState('');

    const NewProject = async(name_arg, desc_arg, image_arg) => {

        const headers = new Headers();
        const data = new FormData();

        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        
        data.append('file', image_arg);
        data.append('name', name_arg);
        data.append('description', desc_arg);

        const new_project = await fetch('/engage/project/v1/projects/new', {
            method: 'POST',
            headers: headers,
            body: data
        });

        return new_project.json();
    }

    const Edit = async(id_arg, new_name_arg, new_desc_arg, new_image_arg) => {

        const headers = new Headers();
        const data = new FormData();

        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        
        data.append('file', new_image_arg !== null ? new_image_arg : project_focus.project_image);
        data.append('name', new_name_arg !== '' ? new_name_arg : project_focus.project_image);
        data.append('description', new_desc_arg !== '' ? new_desc_arg : project_focus.project_description);
        data.append('id', id_arg)

        const new_project = await fetch('/engage/project/v1/projects/edit', {
            method: 'POST',
            headers: headers,
            body: data
        });

        return new_project.json();

    }

    const Delete = async(id_arg, file_arg) => {
        const headers = new Headers();
        headers.append('Authorization', `Bearer ${localStorage.getItem('access_token')}`);
        headers.append('Content-Type', 'application/json');

        const delete_project = await fetch('/engage/project/v1/projects/delete', {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({
                'id': id_arg,
                'file': file_arg
            })
        });

        return delete_project.json();
    }

    useEffect(() => {
        get_projects();
    }, []);

    const HandleChange = (e) => {
        e.preventDefault();
        NewProject(project_name, project_description, project_image)
        .then(result => {
            notifySucc('New project has been added!')
            setTimeout(() => {window.location.reload()}, 1500);
        })
        .catch(err => {
            notifyErr();
            console.error(err);
        })
    }

    const HandleChangeEdit = (e) => {
        e.preventDefault();
        Edit(project_focus.project_id, new_name, new_description, new_image)
        .then(result => {
            notifySucc('Project edit is successful!')
            setTimeout(() => {window.location.reload()}, 1500);
        })
        .catch(err => {
            notifyErr();
            console.error(err);
        })
    }

    return (
        <div className="DashboardContainer">
            <Toaster/>
            <div className="LeftPanel">
                <div className="ListContainer">
                    <h3>Hey there, {admin_name}! </h3>
                    <h2> Project List: </h2>
                    <div className="List">
                        {
                            projects.length <= 0 ?
                            <p>
                                List seems empty 🤔
                            </p>
                            :
                            projects.map((items, key) => 
                            <p
                                key = {key}
                                onClick = {() => {
                                
                                    set_project_focus({
                                    'project_id': items.id,
                                    'project_name': items.name,
                                    'project_desc': items.description,
                                    'project_status': items.status,
                                    'project_image': items.image
                                    });

                                    set_view_project(true);
                                    set_new_project(false);
                                    set_edit_project(false);

                                }}
                            >
                                <u>
                                    {items.name}
                                </u>
                            </p>)
                        }
                    </div>
                    <button 
                        className="NewProject"
                        onClick = {() => {
                            set_new_project(true);
                            set_view_project(false);
                        }}
                    >
                        New Project
                    </button>
                </div>
            </div>
            <div className="RightPanel ProjectBg">
                        {
                            new_project ? 
                                <form 
                                    className = "NewProjectForm"
                                    onSubmit = {HandleChange}
                                    encType = "multipart/form-data"                                   
                                >
                                    <h2> New Project: </h2>
                                    <div className="InputName">
                                        <label htmlFor="project_image">
                                            Project Image
                                        </label>
                                            <br />
                                        <input 
                                            type="file"
                                            name="project_image" 
                                            id="project_image" 
                                            onChange = { e => set_project_image(e.target.files[0])}
                                        />
                                    </div>
                                    <div className="InputName">
                                        <label htmlFor="project_name">
                                            Name
                                        </label>
                                            <br />
                                        <input 
                                            type="text" 
                                            name="project_name" 
                                            id="project_name" 
                                            onChange = { e => set_project_name(e.target.value)}
                                        />
                                    </div>
                                    <div className="InputDesc">
                                        <label htmlFor="project_desc">
                                            Description
                                        </label>
                                            <br />
                                        <input 
                                            type="text" 
                                            name="project_desc" 
                                            id="project_desc" 
                                            onChange = { e => set_project_description(e.target.value)}
                                        />
                                    </div>
                                    <button type="submit">
                                        Proceed Project
                                    </button>
                                </form>
                            :
                                edit_project
                                ?
                                <form 
                                    className = "NewProjectForm"
                                    onSubmit = {HandleChangeEdit}
                                    encType = "multipart/form-data"                                   
                                >
                                    <h2> Edit Project: {project_focus.project_name} </h2>
                                    <div className="InputName">
                                        <label htmlFor="project_image">
                                            Project Image
                                        </label>
                                            <br />
                                        <input 
                                            type="file"
                                            name="project_image" 
                                            id="project_image" 
                                            onChange = { e => set_new_image(e.target.files[0])}                                        
                                        />
                                    </div>
                                    <div className="InputName">
                                        <label htmlFor="project_name">
                                            New Name from [{project_focus.project_name}]
                                        </label>
                                            <br />
                                        <input 
                                            type="text" 
                                            name="project_name" 
                                            id="project_name" 
                                            onChange = { e => set_new_name(e.target.value)}
                                        />
                                    </div>
                                    <div className="InputDesc">
                                        <label htmlFor="project_desc">
                                            New Description from [{project_focus.project_desc}]
                                        </label>
                                            <br />
                                        <input 
                                            type="text" 
                                            name="project_desc" 
                                            id="project_desc" 
                                            onChange = { e => set_new_description(e.target.value)}
                                        />
                                    </div>
                                    <button type="submit">
                                        Proceed Project
                                    </button>
                                </form>
                            :
                                null
                        }
                        {
                            view_project ?
                                <div className="ProjectInfoContainer">
                                    <h2>
                                        <u>
                                            Project Info:
                                        </u>
                                    </h2>
                                    <img src={`/uploads/${project_focus.project_image}`} alt="" className="Thumbnail"/>
                                    <br />
                                    <span>
                                        Project Id: {project_focus.project_id}
                                    </span>
                                        <br />
                                    <span>
                                        Project Name: {project_focus.project_name}
                                    </span>
                                        <br />
                                    <span>
                                        Project Description: {project_focus.project_desc}
                                    </span>
                                        <br />
                                    <span>
                                        Project Status: {project_focus.project_status ? 'ACTIVE' : 'ARCHIVED'}
                                    </span>
                                    <div className="ButtonGroup">
                                        <button
                                        className = "Delete"
                                        onClick = {() => {
                                            Delete(project_focus.project_id, project_focus.project_image)
                                            .then(({status}) => {
                                                notifySucc('Project deletion is successful!')
                                                setTimeout(() => {window.location.reload()}, 1500);                                            
                                            }
                                            )
                                            .catch(error => {
                                                console.error(error);
                                                notifyErr();
                                            })
                                        }}
                                        >
                                            Delete Project
                                        </button>
                                        <button
                                            className = "Edit"
                                            onClick = {() => {
                                                set_edit_project(true);
                                                set_view_project(false);
                                            }}
                                        >
                                            Edit Project
                                        </button>
                                    </div>
                                    </div>
                            :
                                null
                        }
            </div>
        </div>
    );
}

export default Projects;