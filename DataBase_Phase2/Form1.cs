using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Data.SqlClient;

namespace DataBase_Phase2
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            SqlConnection conn = new SqlConnection();
            conn.ConnectionString = "server=LAPTOP-E0CHOJ2J;database=Task_Worker_Matching;integrated security=true"; 
            conn.Open();
            MessageBox.Show(conn.State.ToString()); 
            conn.Close();
            MessageBox.Show(conn.State.ToString()); 
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void button1_Click_1(object sender, EventArgs e)
        {
            this.Hide();
            Form3 newForm = new Form3();
            newForm.Show();

        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Hide();
            Form2 newForm = new Form2();
            newForm.Show();
        }
    }
}
