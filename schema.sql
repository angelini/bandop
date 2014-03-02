--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: hstore; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS hstore WITH SCHEMA public;


--
-- Name: EXTENSION hstore; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION hstore IS 'data type for storing sets of (key, value) pairs';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: algorithm_types; Type: TABLE; Schema: public; Owner: bandop; Tablespace: 
--

CREATE TABLE algorithm_types (
    id integer NOT NULL,
    name character varying,
    defaults hstore
);


ALTER TABLE public.algorithm_types OWNER TO bandop;

--
-- Name: algorithm_types_id_seq; Type: SEQUENCE; Schema: public; Owner: bandop
--

CREATE SEQUENCE algorithm_types_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.algorithm_types_id_seq OWNER TO bandop;

--
-- Name: algorithm_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bandop
--

ALTER SEQUENCE algorithm_types_id_seq OWNED BY algorithm_types.id;


--
-- Name: algorithms; Type: TABLE; Schema: public; Owner: bandop; Tablespace: 
--

CREATE TABLE algorithms (
    id integer NOT NULL,
    type integer,
    config hstore
);


ALTER TABLE public.algorithms OWNER TO bandop;

--
-- Name: algorithms_id_seq; Type: SEQUENCE; Schema: public; Owner: bandop
--

CREATE SEQUENCE algorithms_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.algorithms_id_seq OWNER TO bandop;

--
-- Name: algorithms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bandop
--

ALTER SEQUENCE algorithms_id_seq OWNED BY algorithms.id;


--
-- Name: designs; Type: TABLE; Schema: public; Owner: bandop; Tablespace: 
--

CREATE TABLE designs (
    id integer NOT NULL,
    experiment_id integer,
    name character varying,
    css_file character varying,
    js_file character varying,
    screenshot character varying
);


ALTER TABLE public.designs OWNER TO bandop;

--
-- Name: designs_id_seq; Type: SEQUENCE; Schema: public; Owner: bandop
--

CREATE SEQUENCE designs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.designs_id_seq OWNER TO bandop;

--
-- Name: designs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bandop
--

ALTER SEQUENCE designs_id_seq OWNED BY designs.id;


--
-- Name: experiments; Type: TABLE; Schema: public; Owner: bandop; Tablespace: 
--

CREATE TABLE experiments (
    id integer NOT NULL,
    algorithm_id integer,
    name character varying,
    user_id integer
);


ALTER TABLE public.experiments OWNER TO bandop;

--
-- Name: experiments_id_seq; Type: SEQUENCE; Schema: public; Owner: bandop
--

CREATE SEQUENCE experiments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.experiments_id_seq OWNER TO bandop;

--
-- Name: experiments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bandop
--

ALTER SEQUENCE experiments_id_seq OWNED BY experiments.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: bandop; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
    email character varying,
    password character varying,
    domain character varying
);


ALTER TABLE public.users OWNER TO bandop;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: bandop
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO bandop;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: bandop
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY algorithm_types ALTER COLUMN id SET DEFAULT nextval('algorithm_types_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY algorithms ALTER COLUMN id SET DEFAULT nextval('algorithms_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY designs ALTER COLUMN id SET DEFAULT nextval('designs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY experiments ALTER COLUMN id SET DEFAULT nextval('experiments_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: algorithm_types_pkey; Type: CONSTRAINT; Schema: public; Owner: bandop; Tablespace: 
--

ALTER TABLE ONLY algorithm_types
    ADD CONSTRAINT algorithm_types_pkey PRIMARY KEY (id);


--
-- Name: algorithms_pkey; Type: CONSTRAINT; Schema: public; Owner: bandop; Tablespace: 
--

ALTER TABLE ONLY algorithms
    ADD CONSTRAINT algorithms_pkey PRIMARY KEY (id);


--
-- Name: designs_pkey; Type: CONSTRAINT; Schema: public; Owner: bandop; Tablespace: 
--

ALTER TABLE ONLY designs
    ADD CONSTRAINT designs_pkey PRIMARY KEY (id);


--
-- Name: experiments_pkey; Type: CONSTRAINT; Schema: public; Owner: bandop; Tablespace: 
--

ALTER TABLE ONLY experiments
    ADD CONSTRAINT experiments_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: bandop; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: algorithms_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY algorithms
    ADD CONSTRAINT algorithms_type_fkey FOREIGN KEY (type) REFERENCES algorithm_types(id);


--
-- Name: designs_experiment_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY designs
    ADD CONSTRAINT designs_experiment_id_fkey FOREIGN KEY (experiment_id) REFERENCES experiments(id);


--
-- Name: experiments_algorithm_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY experiments
    ADD CONSTRAINT experiments_algorithm_id_fkey FOREIGN KEY (algorithm_id) REFERENCES algorithms(id);


--
-- Name: experiments_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: bandop
--

ALTER TABLE ONLY experiments
    ADD CONSTRAINT experiments_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: alexangelini
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM alexangelini;
GRANT ALL ON SCHEMA public TO bandop;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

