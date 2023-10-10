attribute vec4 a_position;
uniform mat4 u_projModelView;
uniform vec4 u_color;

varying vec4 v_col;
void main() {
    gl_Position = u_projModelView * a_position;
    v_col = u_color;
}